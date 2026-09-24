# 🚀 Starward Space - 部署与运维配置

本目录用于存放 Starward Space 在生产服务器上的所有部署脚本、反向代理与安全配置。

---

## 目录规划

- `nginx/`: Nginx 核心配置文件（目录遍历防御、隐藏路径拦截、反向代理设置）
- `docker/`: Dockerfile 与 Docker Compose 编排脚本
- `scripts/`: 一键启动/备份脚本
