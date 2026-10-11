<template>
  <div class="space-y-6">
    <!-- 顶部说明看板 -->
    <div class="glass-card p-6 rounded-2xl border border-pink-200/50 dark:border-white/10 flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
      <div class="flex items-center space-x-3">
        <div class="p-3 rounded-2xl bg-amber-500/15 text-amber-600 dark:text-amber-400 border border-amber-400/30">
          <Clapperboard class="w-6 h-6" />
        </div>
        <div>
          <div class="flex items-center space-x-2">
            <h2 class="text-base font-bold text-slate-800 dark:text-white">追番放映厅 · 数据同步中枢</h2>
            <span class="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 border border-emerald-500/30 flex items-center space-x-1">
              <ShieldCheck class="w-3 h-3 mr-0.5" />
              <span>仅列车长鉴权可用</span>
            </span>
          </div>
          <p class="text-xs text-slate-500 dark:text-slate-400 mt-0.5">
            前台已开启只读安全保护（防止公网恶意滥用触发）；在此处统一管理 B 站与 Bangumi 数据更新
          </p>
        </div>
      </div>

      <div class="flex items-center space-x-2 self-end sm:self-auto">
        <button
          @click="loadAllSummaries"
          :disabled="loadingBili || loadingBgm"
          class="px-3.5 py-2 rounded-xl text-xs font-medium border border-pink-200/60 dark:border-white/10 hover:border-amber-400 text-slate-700 dark:text-slate-300 hover:text-amber-600 dark:hover:text-amber-400 transition-all flex items-center space-x-1.5"
        >
          <RotateCw class="w-3.5 h-3.5" :class="{ 'animate-spin': loadingBili || loadingBgm }" />
          <span>刷新看板</span>
        </button>
      </div>
    </div>

    <!-- 核心同步卡片网格 -->
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- 卡片 1：哔哩哔哩放映厅同步 -->
      <div class="glass-card rounded-2xl border border-pink-200/50 dark:border-white/10 p-6 flex flex-col justify-between space-y-6 relative overflow-hidden shadow-sm">
        <div class="space-y-5">
          <!-- 头部 -->
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 rounded-xl bg-[#00aeec]/15 border border-[#00aeec]/30 flex items-center justify-center text-[#00aeec]">
                <Tv class="w-5 h-5" />
              </div>
              <div>
                <h3 class="font-bold text-sm text-slate-800 dark:text-slate-100 flex items-center space-x-1.5">
                  <span>哔哩哔哩 · 追番放映厅</span>
                  <span class="text-[10px] px-2 py-0.5 rounded font-mono bg-[#00aeec]/10 text-[#00aeec]">Bilibili</span>
                </h3>
                <p class="text-xs text-slate-500 dark:text-slate-400">同步个人空间公开关注的番剧与影视剧集</p>
              </div>
            </div>

            <router-link
              to="/bilibili"
              target="_blank"
              class="text-xs text-slate-400 hover:text-[#00aeec] flex items-center space-x-1 transition-colors"
            >
              <span>查看前台</span>
              <ExternalLink class="w-3 h-3" />
            </router-link>
          </div>

          <!-- 统计指标 -->
          <div class="grid grid-cols-3 gap-3">
            <div class="p-3 rounded-xl bg-white/60 dark:bg-white/5 border border-pink-100 dark:border-white/5 text-center">
              <div class="text-lg font-black text-[#00aeec]">{{ biliSummary?.totalCount || 0 }}</div>
              <div class="text-[10px] text-slate-400 mt-0.5">总收录部数</div>
            </div>
            <div class="p-3 rounded-xl bg-white/60 dark:bg-white/5 border border-pink-100 dark:border-white/5 text-center">
              <div class="text-lg font-black text-amber-500">{{ biliSummary?.avgScore || 0 }}★</div>
              <div class="text-[10px] text-slate-400 mt-0.5">全站平均评分</div>
            </div>
            <div class="p-3 rounded-xl bg-white/60 dark:bg-white/5 border border-pink-100 dark:border-white/5 text-center">
              <div class="text-lg font-black text-slate-700 dark:text-slate-200">{{ biliSummary?.typeStats?.length || 0 }}</div>
              <div class="text-[10px] text-slate-400 mt-0.5">覆盖媒体类型</div>
            </div>
          </div>

          <!-- 表单设置与上次同步时间 -->
          <div class="space-y-3 pt-2">
            <div>
              <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
                目标 B 站用户 UID
              </label>
              <div class="flex items-center space-x-2">
                <input
                  v-model="biliUid"
                  type="text"
                  placeholder="28083538"
                  class="flex-1 px-3.5 py-2 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 font-mono focus:outline-none focus:border-[#00aeec]"
                />
                <a
                  :href="`https://space.bilibili.com/${biliUid}`"
                  target="_blank"
                  class="px-3 py-2 rounded-xl text-xs text-slate-500 hover:text-[#00aeec] bg-slate-100 dark:bg-white/5 transition-colors"
                  title="验证个人空间主页"
                >
                  <ExternalLink class="w-3.5 h-3.5" />
                </a>
              </div>
            </div>

            <div class="text-[11px] text-slate-400 flex items-center justify-between">
              <span>上次更新时间：{{ biliSummary?.lastUpdated || '尚未同步' }}</span>
              <span class="text-slate-400">数据表：bilibili_bangumi</span>
            </div>
          </div>
        </div>

        <!-- 同步操作按钮 -->
        <div class="pt-2">
          <button
            @click="handleSyncBilibili"
            :disabled="syncingBili"
            class="w-full py-2.5 rounded-xl bg-[#00aeec] hover:bg-[#009cd3] active:scale-[0.99] text-white font-bold text-xs shadow-md shadow-[#00aeec]/20 transition-all flex items-center justify-center space-x-2 disabled:opacity-60 disabled:cursor-not-allowed"
          >
            <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': syncingBili }" />
            <span>{{ syncingBili ? '正在分页抓取并入库...' : '立即同步 B 站追番数据' }}</span>
          </button>
        </div>
      </div>

      <!-- 卡片 2：Bangumi 番组计划同步 -->
      <div class="glass-card rounded-2xl border border-pink-200/50 dark:border-white/10 p-6 flex flex-col justify-between space-y-6 relative overflow-hidden shadow-sm">
        <div class="space-y-5">
          <!-- 头部 -->
          <div class="flex items-center justify-between">
            <div class="flex items-center space-x-3">
              <div class="w-10 h-10 rounded-xl bg-pink-500/15 border border-pink-500/30 flex items-center justify-center text-pink-500">
                <Sparkles class="w-5 h-5" />
              </div>
              <div>
                <h3 class="font-bold text-sm text-slate-800 dark:text-slate-100 flex items-center space-x-1.5">
                  <span>Bangumi · 番组计划</span>
                  <span class="text-[10px] px-2 py-0.5 rounded font-mono bg-pink-500/10 text-pink-500">bgm.tv</span>
                </h3>
                <p class="text-xs text-slate-500 dark:text-slate-400">同步个人 ACG 评分标记、全站 Rank 与主观短评</p>
              </div>
            </div>

            <router-link
              to="/bangumi"
              target="_blank"
              class="text-xs text-slate-400 hover:text-pink-500 flex items-center space-x-1 transition-colors"
            >
              <span>查看前台</span>
              <ExternalLink class="w-3 h-3" />
            </router-link>
          </div>

          <!-- 统计指标 -->
          <div class="grid grid-cols-3 gap-3">
            <div class="p-3 rounded-xl bg-white/60 dark:bg-white/5 border border-pink-100 dark:border-white/5 text-center">
              <div class="text-lg font-black text-pink-600 dark:text-pink-400">{{ bgmSummary?.totalCount || 0 }}</div>
              <div class="text-[10px] text-slate-400 mt-0.5">总收藏条目</div>
            </div>
            <div class="p-3 rounded-xl bg-white/60 dark:bg-white/5 border border-pink-100 dark:border-white/5 text-center">
              <div class="text-lg font-black text-amber-500">{{ bgmSummary?.avgUserRate || 0 }}★</div>
              <div class="text-[10px] text-slate-400 mt-0.5">我的打分均分</div>
            </div>
            <div class="p-3 rounded-xl bg-white/60 dark:bg-white/5 border border-pink-100 dark:border-white/5 text-center">
              <div class="text-lg font-black text-purple-600 dark:text-purple-400">{{ bgmSummary?.commentCount || 0 }}</div>
              <div class="text-[10px] text-slate-400 mt-0.5">走心吐槽评语</div>
            </div>
          </div>

          <!-- 表单设置与上次同步时间 -->
          <div class="space-y-3 pt-2">
            <div>
              <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
                目标 Bangumi 用户 UID / 用户名
              </label>
              <div class="flex items-center space-x-2">
                <input
                  v-model="bgmUid"
                  type="text"
                  placeholder="978388"
                  class="flex-1 px-3.5 py-2 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 font-mono focus:outline-none focus:border-pink-500"
                />
                <a
                  :href="`https://bgm.tv/user/${bgmUid}`"
                  target="_blank"
                  class="px-3 py-2 rounded-xl text-xs text-slate-500 hover:text-pink-500 bg-slate-100 dark:bg-white/5 transition-colors"
                  title="验证 Bangumi 主页"
                >
                  <ExternalLink class="w-3.5 h-3.5" />
                </a>
              </div>
            </div>

            <div class="text-[11px] text-slate-400 flex items-center justify-between">
              <span>上次更新时间：{{ bgmSummary?.lastUpdated || '尚未同步' }}</span>
              <span class="text-slate-400">数据表：bangumi_collection</span>
            </div>
          </div>
        </div>

        <!-- 同步操作按钮 -->
        <div class="pt-2">
          <button
            @click="handleSyncBangumi"
            :disabled="syncingBgm"
            class="w-full py-2.5 rounded-xl bg-pink-500 hover:bg-pink-600 active:scale-[0.99] text-white font-bold text-xs shadow-md shadow-pink-500/20 transition-all flex items-center justify-center space-x-2 disabled:opacity-60 disabled:cursor-not-allowed"
          >
            <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': syncingBgm }" />
            <span>{{ syncingBgm ? '正在从 bgm.tv 拉取条目与评语...' : '立即同步 Bangumi 数据' }}</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 安全与架构设计科普 -->
    <div class="glass-card rounded-2xl border border-pink-200/40 dark:border-white/10 p-5 bg-gradient-to-r from-pink-50/40 via-transparent to-amber-50/30 dark:from-white/[0.02] dark:to-transparent">
      <div class="flex items-start space-x-3">
        <Info class="w-4 h-4 text-amber-500 mt-0.5 shrink-0" />
        <div class="text-xs space-y-1 text-slate-600 dark:text-slate-400 leading-relaxed">
          <p class="font-bold text-slate-800 dark:text-slate-200">
            星轨安全守则 · 为什么不在前台开放同步按钮？
          </p>
          <p>
            1. <strong>防范恶意重放攻击 (DoS)</strong>：若前台公开触发写入接口，访客或爬虫可能脚本频繁请求，导致你的服务器频繁穿透公网，甚至导致 B 站或 Bangumi 封禁服务器 IP；
          </p>
          <p>
            2. <strong>读写权责严格分离</strong>：前台（/bilibili, /bangumi）负责高质量的只读展示与缓存命中；所有数据写入与全量更新收拢在管理中枢，并由后端拦截器校验 <code class="text-amber-600 dark:text-amber-400 font-mono">X-Admin-Token</code>，安全稳固。
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import {
  Clapperboard,
  ShieldCheck,
  RotateCw,
  RefreshCw,
  Tv,
  Sparkles,
  ExternalLink,
  Info
} from 'lucide-vue-next';
import { getBilibiliSummary, syncBilibili } from '@/api/bilibili';
import { getBangumiSummary, syncBangumi } from '@/api/bangumi';
import type { BilibiliSummary, BangumiSummary } from '@/types';

const biliSummary = ref<BilibiliSummary | null>(null);
const bgmSummary = ref<BangumiSummary | null>(null);

const biliUid = ref('28083538');
const bgmUid = ref('978388');

const loadingBili = ref(false);
const loadingBgm = ref(false);
const syncingBili = ref(false);
const syncingBgm = ref(false);

const loadBiliSummary = async () => {
  loadingBili.value = true;
  try {
    const res = await getBilibiliSummary();
    biliSummary.value = res;
  } catch (error) {
    console.error('加载 B 站统计失败:', error);
  } finally {
    loadingBili.value = false;
  }
};

const loadBgmSummary = async () => {
  loadingBgm.value = true;
  try {
    const res = await getBangumiSummary();
    bgmSummary.value = res;
  } catch (error) {
    console.error('加载 Bangumi 统计失败:', error);
  } finally {
    loadingBgm.value = false;
  }
};

const loadAllSummaries = () => {
  loadBiliSummary();
  loadBgmSummary();
};

const handleSyncBilibili = async () => {
  if (syncingBili.value) return;
  syncingBili.value = true;
  try {
    await syncBilibili(biliUid.value.trim());
    await loadBiliSummary();
    alert('🎉 B 站追番追剧数据同步成功！');
  } catch (error: any) {
    console.error('B 站同步失败:', error);
    alert('同步失败: ' + (error?.message || '请检查控制台'));
  } finally {
    syncingBili.value = false;
  }
};

const handleSyncBangumi = async () => {
  if (syncingBgm.value) return;
  syncingBgm.value = true;
  try {
    await syncBangumi(bgmUid.value.trim());
    await loadBgmSummary();
    alert('🎉 Bangumi 番组条目与评分短评同步成功！');
  } catch (error: any) {
    console.error('Bangumi 同步失败:', error);
    alert('同步失败: ' + (error?.message || '请检查控制台'));
  } finally {
    syncingBgm.value = false;
  }
};

onMounted(() => {
  loadAllSummaries();
});
</script>
