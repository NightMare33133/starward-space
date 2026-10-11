#!/usr/bin/env python3
"""
Bangumi (bgm.tv) 用户全量条目同步脚本
抓取指定 UID 的所有收藏条目（动画、游戏、书籍等），并同步入库 MySQL starward_space.bangumi_collection 表
"""
import urllib.request
import json
import subprocess
import sys
from datetime import datetime

USER_ID = "978388"
API_BASE = "https://api.bgm.tv/v0"
USER_AGENT = "StarwardSpace/1.0 (https://github.com/NightMare33133/starward-space)"

SUBJECT_TYPE_MAP = {
    1: "书籍",
    2: "动画",
    3: "音乐",
    4: "游戏",
    6: "三次元"
}

COLLECTION_TYPE_MAP = {
    1: "想看",
    2: "看过",
    3: "在看",
    4: "搁置",
    5: "抛弃"
}

def escape_sql(val):
    if val is None:
        return "NULL"
    if isinstance(val, (int, float)):
        return str(val)
    val_str = str(val)
    val_str = val_str.replace("\\", "\\\\").replace("'", "''").replace("\0", "")
    return f"'{val_str}'"

def fetch_json(url):
    req = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(req, timeout=15) as resp:
        return json.loads(resp.read().decode("utf-8"))

def parse_iso_datetime(iso_str):
    if not iso_str:
        return None
    try:
        # e.g. 2026-05-18T21:08:57+08:00
        dt = datetime.fromisoformat(iso_str)
        return dt.strftime("%Y-%m-%d %H:%M:%S")
    except Exception:
        return None

def main():
    print(f"[*] 开始获取 Bangumi 用户 {USER_ID} 基本资料...")
    try:
        user_info = fetch_json(f"{API_BASE}/users/{USER_ID}")
        print(f"[+] 用户: {user_info.get('nickname')} (ID: {user_info.get('id')})")
        print(f"[+] 签名: {user_info.get('sign')}")
        # 保存用户资料至后端 resources
        user_vo = {
            "id": user_info.get("id"),
            "username": user_info.get("username"),
            "nickname": user_info.get("nickname"),
            "sign": user_info.get("sign"),
            "avatar": user_info.get("avatar", {}).get("large") or user_info.get("avatar", {}).get("medium"),
            "url": f"https://bgm.tv/user/{USER_ID}"
        }
        with open("starward-backend/src/main/resources/bangumi_profile.json", "w", encoding="utf-8") as f:
            json.dump(user_vo, f, ensure_ascii=False, indent=2)
        print("[+] 用户信息已写入 bangumi_profile.json")
    except Exception as e:
        print(f"[-] 获取用户资料失败: {e}")

    print(f"[*] 开始分页抓取用户收藏条目...")
    offset = 0
    limit = 50
    all_items = []

    while True:
        url = f"{API_BASE}/users/{USER_ID}/collections?limit={limit}&offset={offset}"
        print(f"[*] 请求: {url}")
        res = fetch_json(url)
        items = res.get("data", [])
        total = res.get("total", 0)
        all_items.extend(items)
        print(f"[+] 本批拉取 {len(items)} 条，当前累计 {len(all_items)} / {total} 条")
        if len(all_items) >= total or not items:
            break
        offset += limit

    print(f"[+] 全部条目抓取完成，共 {len(all_items)} 条。正在准备 SQL 写入...")

    sql_statements = []
    for item in all_items:
        subject = item.get("subject", {})
        subject_id = item.get("subject_id") or subject.get("id")
        subject_type = item.get("subject_type") or subject.get("type", 2)
        subject_type_name = SUBJECT_TYPE_MAP.get(subject_type, "其他")
        
        name = subject.get("name", "")
        name_cn = subject.get("name_cn", "") or name
        
        images = subject.get("images", {})
        cover_url = images.get("large") or images.get("common") or images.get("medium") or ""
        score = subject.get("score", 0.0) or 0.0
        rank_num = subject.get("rank", 0) or 0
        eps = subject.get("eps", 0) or 0
        
        collection_type = item.get("type", 2)
        collection_type_name = COLLECTION_TYPE_MAP.get(collection_type, "看过")
        user_rate = item.get("rate", 0) or 0
        user_comment = item.get("comment", "") or ""
        air_date = subject.get("date", "") or ""
        
        # 标签处理 (前 5 个最热标签)
        tags_raw = subject.get("tags", [])
        tag_names = [t.get("name") for t in tags_raw[:5] if t.get("name")]
        tags_str = ", ".join(tag_names)
        
        short_summary = subject.get("short_summary", "") or ""
        collection_updated_at = parse_iso_datetime(item.get("updated_at"))

        sql = f"""
INSERT INTO bangumi_collection (
    subject_id, subject_type, subject_type_name, name, name_cn, cover_url,
    score, rank_num, eps, collection_type, collection_type_name,
    user_rate, user_comment, air_date, tags, short_summary, collection_updated_at
) VALUES (
    {subject_id}, {subject_type}, {escape_sql(subject_type_name)}, {escape_sql(name)}, {escape_sql(name_cn)}, {escape_sql(cover_url)},
    {score}, {rank_num}, {eps}, {collection_type}, {escape_sql(collection_type_name)},
    {user_rate}, {escape_sql(user_comment)}, {escape_sql(air_date)}, {escape_sql(tags_str)}, {escape_sql(short_summary)}, {escape_sql(collection_updated_at)}
) ON DUPLICATE KEY UPDATE
    subject_type = VALUES(subject_type),
    subject_type_name = VALUES(subject_type_name),
    name = VALUES(name),
    name_cn = VALUES(name_cn),
    cover_url = VALUES(cover_url),
    score = VALUES(score),
    rank_num = VALUES(rank_num),
    eps = VALUES(eps),
    collection_type = VALUES(collection_type),
    collection_type_name = VALUES(collection_type_name),
    user_rate = VALUES(user_rate),
    user_comment = VALUES(user_comment),
    air_date = VALUES(air_date),
    tags = VALUES(tags),
    short_summary = VALUES(short_summary),
    collection_updated_at = VALUES(collection_updated_at);
"""
        sql_statements.append(sql.strip())

    full_sql = "BEGIN;\n" + "\n".join(sql_statements) + "\nCOMMIT;\n"
    
    # 写入 MySQL
    proc = subprocess.run(
        ["mysql", "-u", "root", "-p123456", "starward_space"],
        input=full_sql,
        text=True,
        capture_output=True
    )
    if proc.returncode != 0:
        print(f"[-] MySQL 写入失败: {proc.stderr}")
        sys.exit(1)
    
    print(f"✅ 成功同步 {len(all_items)} 条 Bangumi 收藏记录到 MySQL 数据库！")

if __name__ == "__main__":
    main()
