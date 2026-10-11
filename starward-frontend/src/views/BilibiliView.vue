<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
    <!-- 顶部标题与个人主页卡片 -->
    <div class="glass-card p-6 sm:p-8 rounded-3xl border border-pink-200/50 dark:border-white/10 relative overflow-hidden shadow-xl">
      <!-- 背景光晕弥散 -->
      <div class="absolute -right-16 -top-16 w-64 h-64 rounded-full bg-gradient-to-br from-[#00aeec]/20 via-pink-400/15 to-transparent blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-6">
        <!-- 左侧：图标与标题 -->
        <div class="flex items-start sm:items-center space-x-4">
          <div class="w-14 h-14 sm:w-16 sm:h-16 rounded-2xl bg-[#00aeec]/10 dark:bg-[#00aeec]/20 border border-[#00aeec]/30 flex items-center justify-center shrink-0 shadow-lg shadow-[#00aeec]/15">
            <!-- 哔哩哔哩经典小电视 SVG -->
            <svg class="w-8 h-8 sm:w-9 sm:h-9 text-[#00aeec]" viewBox="0 0 24 24" fill="currentColor">
              <path d="M17.813 4.653h.854c1.51 0 2.733 1.224 2.733 2.734v9.98c0 1.51-1.223 2.734-2.733 2.734H5.333c-1.51 0-2.733-1.224-2.733-2.734V7.387c0-1.51 1.223-2.734 2.733-2.734h.854L4.85 3.315a.75.75 0 0 1 1.06-1.06l2.12 2.12h7.94l2.12-2.12a.75.75 0 1 1 1.06 1.06l-1.337 1.338zM5.333 6.153a1.233 1.233 0 0 0-1.233 1.234v9.98c0 .681.552 1.234 1.233 1.234h13.334c.68 0 1.233-.553 1.233-1.234V7.387c0-.681-.552-1.234-1.233-1.234H5.333zm3.167 4.5a1.25 1.25 0 1 1 0 2.5 1.25 1.25 0 0 1 0-2.5zm7 0a1.25 1.25 0 1 1 0 2.5 1.25 1.25 0 0 1 0-2.5z"/>
            </svg>
          </div>

          <div class="space-y-1">
            <div class="flex items-center space-x-2.5 flex-wrap">
              <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-800 dark:text-white tracking-tight">
                哔哩哔哩 · 追番放映厅
              </h1>
              <span class="px-2.5 py-0.5 rounded-full text-xs font-mono font-semibold bg-[#00aeec]/15 text-[#00aeec] border border-[#00aeec]/30">
                Bilibili Anime & Drama
              </span>
            </div>
            <p class="text-xs sm:text-sm text-slate-500 dark:text-slate-400">
              记录我的 B 站追番漫游足迹、经典动画、剧场版与灵感宇宙。
            </p>
          </div>
        </div>

        <!-- 右侧：B站主页跳转与一键同步 -->
        <div class="flex items-center space-x-3 self-start md:self-auto shrink-0">
          <!-- 直达 B 站个人主页 -->
          <a
            href="https://space.bilibili.com/28083538"
            target="_blank"
            rel="noopener noreferrer"
            class="px-3.5 py-2 rounded-xl glass-card border border-pink-200/60 dark:border-white/10 hover:border-[#00aeec]/50 text-xs font-medium text-slate-700 dark:text-slate-200 hover:text-[#00aeec] dark:hover:text-[#00aeec] transition-all flex items-center space-x-1.5 shadow-sm"
            title="访问我的哔哩哔哩个人空间"
          >
            <ExternalLink class="w-3.5 h-3.5" />
            <span>B站主页 (UID: 28083538)</span>
          </a>

          <!-- 同步按钮 -->
          <button
            @click="handleSync"
            :disabled="syncing"
            class="px-3.5 py-2 rounded-xl bg-gradient-to-r from-[#00aeec] to-pink-500 hover:opacity-95 text-white text-xs font-bold transition-all flex items-center space-x-1.5 shadow-md shadow-[#00aeec]/20 disabled:opacity-60"
            title="从哔哩哔哩同步最新追番"
          >
            <RefreshCw class="w-3.5 h-3.5" :class="{ 'animate-spin': syncing }" />
            <span>{{ syncing ? '同步中...' : '同步追番' }}</span>
          </button>
        </div>
      </div>

      <!-- 统计数据看板 -->
      <div class="grid grid-cols-2 sm:grid-cols-4 gap-3.5 mt-6 pt-6 border-t border-pink-200/40 dark:border-white/5">
        <div class="p-3 rounded-2xl bg-white/50 dark:bg-white/5 border border-pink-200/40 dark:border-white/5 flex items-center space-x-3">
          <div class="p-2.5 rounded-xl bg-pink-500/15 text-pink-600 dark:text-nebula-pink">
            <Film class="w-4 h-4" />
          </div>
          <div>
            <p class="text-[11px] text-slate-400">总追番追剧</p>
            <p class="text-lg font-bold text-slate-800 dark:text-white font-mono">{{ summary?.totalCount || bilibiliItems.length }} <span class="text-xs font-normal text-slate-400">部</span></p>
          </div>
        </div>

        <div class="p-3 rounded-2xl bg-white/50 dark:bg-white/5 border border-pink-200/40 dark:border-white/5 flex items-center space-x-3">
          <div class="p-2.5 rounded-xl bg-amber-400/15 text-amber-600 dark:text-amber-400">
            <Star class="w-4 h-4 fill-amber-400" />
          </div>
          <div>
            <p class="text-[11px] text-slate-400">Bilibili 均分</p>
            <p class="text-lg font-bold text-slate-800 dark:text-white font-mono">{{ summary?.avgScore || '9.2' }} <span class="text-xs font-normal text-slate-400">分</span></p>
          </div>
        </div>

        <div class="p-3 rounded-2xl bg-white/50 dark:bg-white/5 border border-pink-200/40 dark:border-white/5 flex items-center space-x-3">
          <div class="p-2.5 rounded-xl bg-sky-500/15 text-sky-600 dark:text-nebula-cyan">
            <Tv class="w-4 h-4" />
          </div>
          <div>
            <p class="text-[11px] text-slate-400">日本动画 (番剧)</p>
            <p class="text-lg font-bold text-slate-800 dark:text-white font-mono">{{ getTypeCount('番剧') }} <span class="text-xs font-normal text-slate-400">部</span></p>
          </div>
        </div>

        <div class="p-3 rounded-2xl bg-white/50 dark:bg-white/5 border border-pink-200/40 dark:border-white/5 flex items-center space-x-3">
          <div class="p-2.5 rounded-xl bg-emerald-500/15 text-emerald-600 dark:text-emerald-400">
            <Clock class="w-4 h-4" />
          </div>
          <div>
            <p class="text-[11px] text-slate-400">数据同步于</p>
            <p class="text-xs font-bold text-slate-700 dark:text-slate-300 font-mono mt-1 truncate" :title="summary?.lastUpdated">
              {{ summary?.lastUpdated || '刚刚' }}
            </p>
          </div>
        </div>
      </div>
    </div>

    <!-- 筛选、搜索与排序控制栏 -->
    <div class="space-y-4">
      <div class="flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4">
        <!-- 分类切换 Pills -->
        <div class="flex items-center space-x-1.5 overflow-x-auto pb-1 md:pb-0 scrollbar-none">
          <button
            @click="selectedType = 'ALL'"
            class="px-3.5 py-1.5 rounded-full text-xs font-medium transition-all shrink-0 flex items-center space-x-1"
            :class="selectedType === 'ALL'
              ? 'bg-[#00aeec] text-white font-bold shadow-md shadow-[#00aeec]/20'
              : 'glass-card border border-pink-200/60 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          >
            <span>全部</span>
            <span class="text-[10px] opacity-80 font-mono">({{ summary?.totalCount || bilibiliItems.length }})</span>
          </button>

          <button
            v-for="stat in summary?.typeStats || []"
            :key="stat.name"
            @click="selectedType = stat.name"
            class="px-3.5 py-1.5 rounded-full text-xs font-medium transition-all shrink-0 flex items-center space-x-1"
            :class="selectedType === stat.name
              ? 'bg-[#00aeec] text-white font-bold shadow-md shadow-[#00aeec]/20'
              : 'glass-card border border-pink-200/60 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          >
            <span>{{ stat.name }}</span>
            <span class="text-[10px] opacity-80 font-mono">({{ stat.count }})</span>
          </button>
        </div>

        <!-- 搜索与排序 -->
        <div class="flex items-center space-x-3">
          <!-- 搜索框 -->
          <div class="relative flex-1 sm:w-60">
            <Search class="w-3.5 h-3.5 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="搜索番剧名称或剧情..."
              class="w-full pl-8 pr-8 py-1.5 rounded-xl glass-card bg-white/80 dark:bg-space-950/70 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 focus:outline-none focus:border-[#00aeec] transition-colors"
            />
            <button
              v-if="searchKeyword"
              @click="searchKeyword = ''"
              class="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-white"
            >
              <X class="w-3.5 h-3.5" />
            </button>
          </div>

          <!-- 排序选择 -->
          <select
            v-model="sortOrder"
            class="px-3 py-1.5 rounded-xl glass-card bg-white/80 dark:bg-space-950/70 border border-pink-200/70 dark:border-white/10 text-xs text-slate-700 dark:text-slate-200 focus:outline-none focus:border-[#00aeec] shrink-0 font-mono cursor-pointer"
          >
            <option value="score_desc">评分最高 ★</option>
            <option value="count_desc">集数最多 📦</option>
            <option value="updated_desc">最新收录 ⌛</option>
          </select>
        </div>
      </div>
    </div>

    <!-- 加载中状态 -->
    <div v-if="loading" class="py-20 flex flex-col items-center justify-center space-y-3">
      <Loader2 class="w-8 h-8 animate-spin text-[#00aeec]" />
      <span class="text-xs text-slate-500 font-mono">正在从星向数据引擎加载 B 站追番...</span>
    </div>

    <!-- 空数据状态 -->
    <div v-else-if="filteredItems.length === 0" class="py-24 text-center space-y-3">
      <Tv class="w-12 h-12 mx-auto text-slate-400/60 dark:text-slate-600" />
      <p class="text-sm font-medium text-slate-600 dark:text-slate-400">
        {{ searchKeyword ? '没有找到符合关键词的番剧' : '暂无相关追番记录' }}
      </p>
      <button
        v-if="searchKeyword"
        @click="searchKeyword = ''"
        class="text-xs text-[#00aeec] hover:underline"
      >
        清空搜索条件
      </button>
    </div>

    <!-- 番剧海报网格 (Poster Grid) -->
    <div v-else class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6 gap-4 sm:gap-5">
      <a
        v-for="item in filteredItems"
        :key="item.id"
        :href="item.url"
        target="_blank"
        rel="noopener noreferrer"
        class="group glass-card rounded-2xl border border-pink-200/50 dark:border-white/10 overflow-hidden flex flex-col transition-all duration-300 hover:-translate-y-1.5 hover:shadow-xl hover:shadow-[#00aeec]/10 hover:border-[#00aeec]/40 relative"
      >
        <!-- 海报图片容器 (强制 no-referrer 防盗链) -->
        <div class="relative w-full aspect-[3/4.2] overflow-hidden bg-slate-200 dark:bg-space-900">
          <img
            :src="item.cover"
            :alt="item.title"
            referrerpolicy="no-referrer"
            loading="lazy"
            decoding="async"
            class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-105"
            onerror="this.src='/images/hsr/himeko_express.webp'"
          />

          <!-- 顶部标签与评分浮层 -->
          <div class="absolute top-2 left-2 right-2 flex items-center justify-between pointer-events-none">
            <!-- 分类徽章 -->
            <span
              class="px-2 py-0.5 rounded-lg text-[10px] font-bold text-white shadow-md backdrop-blur-md"
              :class="getTypeBadgeColor(item.seasonTypeName)"
            >
              {{ item.seasonTypeName }}
            </span>

            <!-- 评分 (大于0才显示) -->
            <span
              v-if="item.score && item.score > 0"
              class="px-2 py-0.5 rounded-lg text-[10px] font-mono font-bold bg-black/60 backdrop-blur-md text-amber-300 border border-amber-400/30 flex items-center space-x-0.5 shadow-md"
            >
              <Star class="w-2.5 h-2.5 fill-amber-300" />
              <span>{{ item.score }}</span>
            </span>
          </div>

          <!-- 底部集数状态蒙层 (全24话 / 更新至...) -->
          <div class="absolute bottom-0 inset-x-0 p-2 bg-gradient-to-t from-black/85 via-black/40 to-transparent flex items-center justify-between text-[11px] font-mono text-white/90">
            <span class="truncate pr-1 text-[10px]">{{ item.indexShow || (item.totalCount ? '全' + item.totalCount + '话' : '连载中') }}</span>
            <span v-if="item.badge" class="px-1.5 py-0.2 rounded text-[9px] bg-rose-500 text-white font-bold shrink-0">
              {{ item.badge }}
            </span>
          </div>
        </div>

        <!-- 卡片底部信息 -->
        <div class="p-3 flex-1 flex flex-col justify-between space-y-1">
          <h3
            class="text-xs sm:text-sm font-bold text-slate-800 dark:text-slate-100 group-hover:text-[#00aeec] transition-colors line-clamp-2 leading-snug"
            :title="item.title"
          >
            {{ item.title }}
          </h3>

          <p
            v-if="item.evaluate"
            class="text-[11px] text-slate-400 dark:text-slate-500 line-clamp-2 leading-relaxed"
            :title="item.evaluate"
          >
            {{ item.evaluate }}
          </p>
        </div>
      </a>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import {
  Film,
  Star,
  Tv,
  Clock,
  ExternalLink,
  RefreshCw,
  Search,
  X,
  Loader2
} from 'lucide-vue-next';
import { getBilibiliList, getBilibiliSummary, syncBilibili } from '@/api/bilibili';
import type { BilibiliBangumi, BilibiliSummary } from '@/types';
import { useToast } from '@/composables/useToast';

const toast = useToast();
const bilibiliItems = ref<BilibiliBangumi[]>([]);
const summary = ref<BilibiliSummary | null>(null);
const loading = ref(false);
const syncing = ref(false);

const selectedType = ref('ALL');
const searchKeyword = ref('');
const sortOrder = ref('score_desc');

const getTypeCount = (typeName: string) => {
  if (!summary.value?.typeStats) return 0;
  const match = summary.value.typeStats.find(s => s.name === typeName);
  return match ? match.count : 0;
};

const getTypeBadgeColor = (typeName: string) => {
  switch (typeName) {
    case '番剧':
      return 'bg-sky-500/80';
    case '国创':
      return 'bg-emerald-500/80';
    case '电影':
      return 'bg-purple-500/80';
    case '纪录片':
      return 'bg-amber-500/80';
    case '综艺':
      return 'bg-pink-500/80';
    default:
      return 'bg-slate-600/80';
  }
};

const fetchList = async () => {
  loading.value = true;
  try {
    const list = await getBilibiliList({
      type: selectedType.value === 'ALL' ? undefined : selectedType.value,
      keyword: searchKeyword.value.trim() || undefined,
      sort: sortOrder.value
    });
    bilibiliItems.value = list || [];
  } catch (err: any) {
    console.error('获取哔哩哔哩追番数据失败:', err);
    toast.error('获取追番数据失败，请检查网络');
  } finally {
    loading.value = false;
  }
};

const fetchSummary = async () => {
  try {
    const s = await getBilibiliSummary();
    summary.value = s;
  } catch (err: any) {
    console.error('获取追番统计失败:', err);
  }
};

const filteredItems = computed(() => {
  return bilibiliItems.value.filter(item => {
    if (selectedType.value !== 'ALL' && item.seasonTypeName !== selectedType.value) {
      return false;
    }
    if (searchKeyword.value.trim()) {
      const q = searchKeyword.value.toLowerCase();
      const matchTitle = item.title?.toLowerCase().includes(q);
      const matchEval = item.evaluate?.toLowerCase().includes(q);
      return matchTitle || matchEval;
    }
    return true;
  });
});

const handleSync = async () => {
  syncing.value = true;
  try {
    toast.info('正在从哔哩哔哩同步追番数据，请稍候...');
    await syncBilibili('28083538');
    toast.success('B 站追番数据同步成功 🎬');
    await Promise.all([fetchSummary(), fetchList()]);
  } catch (err: any) {
    toast.error(err.response?.data?.message || err.message || '同步失败');
  } finally {
    syncing.value = false;
  }
};

onMounted(() => {
  fetchSummary();
  fetchList();
});
</script>
