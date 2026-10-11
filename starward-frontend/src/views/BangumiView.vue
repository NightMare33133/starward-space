<template>
  <div class="min-h-screen pt-24 pb-20 px-4 sm:px-6 lg:px-8 max-w-7xl mx-auto">
    <!-- 顶部 Banner：Bangumi 档案与统计看板 -->
    <div class="relative overflow-hidden rounded-3xl glass-card border border-pink-200/50 dark:border-white/10 p-6 sm:p-8 mb-8 shadow-xl">
      <!-- 背景光晕装饰 -->
      <div class="absolute -top-24 -right-24 w-96 h-96 bg-gradient-to-br from-pink-500/20 via-purple-500/15 to-transparent rounded-full blur-3xl pointer-events-none"></div>
      <div class="absolute -bottom-24 -left-24 w-80 h-80 bg-gradient-to-tr from-cyan-500/15 via-blue-500/10 to-transparent rounded-full blur-3xl pointer-events-none"></div>

      <div class="relative z-10 flex flex-col lg:flex-row items-center justify-between gap-6">
        <!-- 用户身份信息 -->
        <div class="flex flex-col sm:flex-row items-center sm:items-start text-center sm:text-left gap-5">
          <!-- 头像 -->
          <div class="relative group">
            <div class="w-24 h-24 sm:w-28 sm:h-28 rounded-2xl p-1 bg-gradient-to-tr from-pink-500 via-purple-500 to-cyan-400 shadow-xl shadow-pink-500/20">
              <img
                :src="profile.avatar || 'https://lain.bgm.tv/pic/user/l/000/97/83/978388.jpg'"
                :alt="profile.nickname"
                referrerpolicy="no-referrer"
                class="w-full h-full object-cover rounded-[14px]"
              />
            </div>
            <span class="absolute -bottom-2 -right-2 px-2 py-0.5 rounded-full text-[10px] font-bold bg-pink-500 text-white shadow-md">
              bgm.tv
            </span>
          </div>

          <!-- 文本信息 -->
          <div class="flex flex-col space-y-2 max-w-md">
            <div class="flex flex-wrap items-center justify-center sm:justify-start gap-2">
              <h1 class="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-800 dark:text-slate-100">
                {{ profile.nickname || 'NightMare丶明眸' }}
              </h1>
              <span class="text-xs px-2.5 py-0.5 rounded-full font-mono bg-pink-100/80 text-pink-700 dark:bg-pink-900/40 dark:text-pink-300 font-semibold border border-pink-200 dark:border-pink-800/50">
                UID: {{ profile.id || 978388 }}
              </span>
            </div>

            <!-- 个性签名引言 -->
            <div class="flex items-center justify-center sm:justify-start space-x-1.5 text-xs text-slate-500 dark:text-slate-400 italic">
              <Quote class="w-3.5 h-3.5 text-pink-400 shrink-0" />
              <span>{{ profile.sign || '纯主观打分，别骂我QAQ' }}</span>
            </div>

            <!-- 外链主页与说明 -->
            <div class="flex items-center justify-center sm:justify-start space-x-3 pt-1">
              <a
                :href="profile.url || 'https://bgm.tv/user/978388'"
                target="_blank"
                rel="noopener noreferrer"
                class="inline-flex items-center space-x-1.5 text-xs font-medium text-pink-600 hover:text-pink-700 dark:text-nebula-cyan dark:hover:text-cyan-300 transition-colors"
              >
                <span>Bangumi 主页</span>
                <ExternalLink class="w-3.5 h-3.5" />
              </a>
              <span class="text-slate-300 dark:text-slate-700">•</span>
              <span class="text-xs text-slate-400">上次同步: {{ summary.lastUpdated || '刚刚' }}</span>
            </div>
          </div>
        </div>

        <!-- 统计核心卡片区与一键同步 -->
        <div class="flex flex-wrap items-center justify-center gap-3 sm:gap-4">
          <div class="px-4 py-3 rounded-2xl bg-white/70 dark:bg-white/5 border border-pink-100 dark:border-white/5 shadow-sm text-center min-w-[90px]">
            <div class="text-xl sm:text-2xl font-black text-pink-600 dark:text-pink-400">{{ summary.totalCount || 0 }}</div>
            <div class="text-[11px] text-slate-500 dark:text-slate-400">总标记条目</div>
          </div>

          <div class="px-4 py-3 rounded-2xl bg-white/70 dark:bg-white/5 border border-pink-100 dark:border-white/5 shadow-sm text-center min-w-[90px]">
            <div class="text-xl sm:text-2xl font-black text-amber-500 flex items-center justify-center">
              <Star class="w-4 h-4 fill-amber-400 text-amber-400 mr-0.5" />
              {{ summary.avgUserRate || 0 }}
            </div>
            <div class="text-[11px] text-slate-500 dark:text-slate-400">主观打分均分</div>
          </div>

          <div class="px-4 py-3 rounded-2xl bg-white/70 dark:bg-white/5 border border-pink-100 dark:border-white/5 shadow-sm text-center min-w-[90px]">
            <div class="text-xl sm:text-2xl font-black text-purple-600 dark:text-purple-400 flex items-center justify-center">
              <Sparkles class="w-4 h-4 text-purple-400 mr-0.5" />
              {{ summary.avgScore || 0 }}
            </div>
            <div class="text-[11px] text-slate-500 dark:text-slate-400">全站评分均分</div>
          </div>

          <div class="px-4 py-3 rounded-2xl bg-white/70 dark:bg-white/5 border border-pink-100 dark:border-white/5 shadow-sm text-center min-w-[90px]">
            <div class="text-xl sm:text-2xl font-black text-cyan-600 dark:text-cyan-400">{{ summary.commentCount || 0 }}</div>
            <div class="text-[11px] text-slate-500 dark:text-slate-400">走心评语</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 筛选过滤与控制栏 -->
    <div class="glass-card rounded-2xl border border-pink-200/50 dark:border-white/10 p-4 sm:p-5 mb-8 space-y-4">
      <div class="flex flex-col md:flex-row items-stretch md:items-center justify-between gap-4">
        <!-- 条目类型 Tabs (全部 / 动画 / 游戏) -->
        <div class="flex items-center space-x-1.5 overflow-x-auto pb-1 md:pb-0 scrollbar-none">
          <button
            v-for="st in subjectTypeOptions"
            :key="st.value"
            @click="selectedSubjectType = st.value"
            class="px-3.5 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap transition-all"
            :class="selectedSubjectType === st.value ? 'bg-pink-500 text-white shadow-md shadow-pink-500/20' : 'bg-white/60 dark:bg-white/5 text-slate-600 dark:text-slate-300 hover:bg-pink-100/50'"
          >
            {{ st.label }}
            <span v-if="st.count !== undefined" class="ml-1 opacity-75 text-[10px]">({{ st.count }})</span>
          </button>
        </div>

        <!-- 搜索框与排序下拉 -->
        <div class="flex items-center gap-3">
          <div class="relative flex-1 sm:w-64">
            <Search class="w-4 h-4 absolute left-3 top-1/2 -translate-y-1/2 text-slate-400" />
            <input
              v-model="keyword"
              type="text"
              placeholder="搜索条目、标签或评语..."
              class="w-full pl-9 pr-8 py-1.5 rounded-xl text-xs bg-white/70 dark:bg-white/5 border border-pink-100 dark:border-white/10 text-slate-800 dark:text-slate-100 focus:outline-none focus:ring-2 focus:ring-pink-500/30 transition-all placeholder:text-slate-400"
            />
            <button
              v-if="keyword"
              @click="keyword = ''"
              class="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200"
            >
              <X class="w-3.5 h-3.5" />
            </button>
          </div>

          <!-- 排序规则 -->
          <div class="relative">
            <select
              v-model="sortRule"
              class="appearance-none pl-3 pr-8 py-1.5 rounded-xl text-xs bg-white/70 dark:bg-white/5 border border-pink-100 dark:border-white/10 text-slate-700 dark:text-slate-200 focus:outline-none focus:ring-2 focus:ring-pink-500/30 font-medium cursor-pointer"
            >
              <option value="user_rate_desc">我的打分优先 ★</option>
              <option value="score_desc">全站评分最高</option>
              <option value="rank_asc">全站排名榜首</option>
              <option value="air_date_desc">放送时间最近</option>
              <option value="updated_desc">收藏更新时间</option>
            </select>
            <ChevronDown class="w-3.5 h-3.5 absolute right-2.5 top-1/2 -translate-y-1/2 pointer-events-none text-slate-400" />
          </div>
        </div>
      </div>

      <!-- 收藏状态二级筛选 (全部 / 看过 / 在看 / 想看 / 搁置) -->
      <div class="flex items-center space-x-1.5 pt-1 overflow-x-auto border-t border-pink-100/60 dark:border-white/5 scrollbar-none">
        <span class="text-[11px] text-slate-400 shrink-0 mr-1 flex items-center">
          <Filter class="w-3 h-3 mr-1" /> 状态:
        </span>
        <button
          v-for="ct in collectionTypeOptions"
          :key="ct.value"
          @click="selectedCollectionType = ct.value"
          class="px-2.5 py-1 rounded-lg text-xs font-medium whitespace-nowrap transition-all"
          :class="selectedCollectionType === ct.value ? 'bg-pink-100 text-pink-700 dark:bg-pink-900/40 dark:text-pink-300 font-bold' : 'text-slate-500 hover:text-slate-800 dark:hover:text-slate-200'"
        >
          {{ ct.label }}
          <span v-if="ct.count !== undefined" class="ml-0.5 text-[10px] opacity-75">({{ ct.count }})</span>
        </button>
      </div>
    </div>

    <!-- 加载中动画 -->
    <div v-if="loading" class="py-20 flex flex-col items-center justify-center space-y-4">
      <div class="w-12 h-12 rounded-full border-4 border-pink-500/20 border-t-pink-500 animate-spin"></div>
      <p class="text-xs text-slate-500 dark:text-slate-400 font-medium">正在拉取 Bangumi 条目与打分记录...</p>
    </div>

    <!-- 空数据提示 -->
    <div v-else-if="filteredCollections.length === 0" class="py-20 text-center glass-card rounded-2xl border border-pink-100 dark:border-white/5 p-8">
      <Clapperboard class="w-12 h-12 mx-auto text-pink-300 dark:text-pink-800 mb-3" />
      <h3 class="text-base font-bold text-slate-700 dark:text-slate-200">未找到符合条件的条目</h3>
      <p class="text-xs text-slate-400 mt-1">尝试更换搜索词或重置筛选状态</p>
      <button
        @click="resetFilters"
        class="mt-4 px-4 py-1.5 rounded-full text-xs bg-pink-500 text-white font-medium hover:bg-pink-600 transition-colors"
      >
        重置筛选
      </button>
    </div>

    <!-- 收藏卡片展示网格 -->
    <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      <div
        v-for="item in filteredCollections"
        :key="item.id"
        @click="openModal(item)"
        class="group glass-card rounded-2xl border border-pink-200/40 dark:border-white/10 overflow-hidden shadow-md hover:shadow-2xl hover:border-pink-400/60 dark:hover:border-pink-500/40 transition-all duration-300 flex flex-col cursor-pointer hover:-translate-y-1.5"
      >
        <!-- 封面区域 -->
        <div class="relative aspect-[3/4] w-full overflow-hidden bg-slate-900/60">
          <img
            :src="item.coverUrl"
            :alt="item.nameCn || item.name"
            referrerpolicy="no-referrer"
            loading="lazy"
            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
            @error="onImageError($event)"
          />

          <!-- 遮罩渐变 -->
          <div class="absolute inset-0 bg-gradient-to-t from-black/80 via-black/20 to-transparent"></div>

          <!-- 顶部状态与类型徽章 -->
          <div class="absolute top-2.5 inset-x-2.5 flex items-center justify-between pointer-events-none">
            <!-- 类型徽章 -->
            <span
              class="px-2 py-0.5 rounded-md text-[10px] font-bold backdrop-blur-md shadow"
              :class="item.subjectType === 4 ? 'bg-indigo-600/90 text-white' : 'bg-pink-600/90 text-white'"
            >
              {{ item.subjectTypeName }}
            </span>

            <!-- Bangumi 全站 Rank 排名 (若上榜) -->
            <span
              v-if="item.rankNum && item.rankNum > 0"
              class="px-2 py-0.5 rounded-md text-[10px] font-extrabold backdrop-blur-md shadow flex items-center space-x-0.5"
              :class="item.rankNum <= 100 ? 'bg-amber-400 text-slate-900' : 'bg-slate-900/80 text-amber-300 border border-amber-400/40'"
            >
              <Trophy class="w-3 h-3 mr-0.5" />
              #{{ item.rankNum }}
            </span>
          </div>

          <!-- 封面底部：评分对比 -->
          <div class="absolute bottom-2.5 inset-x-2.5 flex items-end justify-between pointer-events-none">
            <!-- 用户主观打分 (核心高亮) -->
            <div v-if="item.userRate && item.userRate > 0" class="flex items-center space-x-1 px-2.5 py-1 rounded-xl bg-pink-600/90 text-white backdrop-blur-md shadow-lg">
              <Star class="w-3.5 h-3.5 fill-amber-300 text-amber-300" />
              <span class="text-xs font-black tracking-tight">{{ item.userRate }}</span>
              <span class="text-[9px] opacity-80">/10 我的打分</span>
            </div>
            <div v-else class="text-[10px] text-white/60 px-2 py-0.5 rounded-md bg-black/40 backdrop-blur-sm">未打分</div>

            <!-- 全站官方评分 -->
            <div v-if="item.score && item.score > 0" class="flex items-center space-x-1 px-2 py-0.5 rounded-lg bg-black/60 text-amber-300 backdrop-blur-md text-[11px] font-bold">
              <span>★ {{ item.score }}</span>
            </div>
          </div>
        </div>

        <!-- 内容区域 -->
        <div class="p-4 flex-1 flex flex-col justify-between space-y-3">
          <!-- 标题区 -->
          <div class="space-y-1">
            <h3 class="font-bold text-sm text-slate-800 dark:text-slate-100 line-clamp-1 group-hover:text-pink-600 dark:group-hover:text-pink-400 transition-colors" :title="item.nameCn || item.name">
              {{ item.nameCn || item.name }}
            </h3>
            <p v-if="item.name && item.name !== item.nameCn" class="text-[11px] text-slate-400 dark:text-slate-500 line-clamp-1 font-mono" :title="item.name">
              {{ item.name }}
            </p>
          </div>

          <!-- 用户的走心评语气泡 (高亮展示) -->
          <div
            v-if="item.userComment"
            class="relative p-2.5 rounded-xl bg-pink-50/70 dark:bg-white/[0.04] border border-pink-100 dark:border-white/5 text-xs text-slate-700 dark:text-slate-300 italic"
          >
            <Quote class="w-3 h-3 text-pink-400 absolute top-1.5 left-1.5 opacity-60" />
            <p class="line-clamp-2 pl-3.5 text-[11px] leading-relaxed">
              {{ item.userComment }}
            </p>
          </div>

          <!-- 底部标签与话数 -->
          <div class="pt-2 border-t border-pink-100/60 dark:border-white/5 flex items-center justify-between text-[11px] text-slate-400">
            <div class="flex items-center space-x-1.5 truncate">
              <Calendar class="w-3 h-3 shrink-0" />
              <span class="truncate">{{ item.airDate || '未知' }}</span>
            </div>
            <span v-if="item.eps && item.eps > 0" class="shrink-0 px-1.5 py-0.5 rounded bg-slate-100 dark:bg-white/5 text-[10px]">
              {{ item.eps }} 话
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 条目详情弹窗 Modal -->
    <div
      v-if="activeModalItem"
      class="fixed inset-0 z-50 flex items-center justify-center p-4 sm:p-6 bg-black/70 backdrop-blur-md animate-fade-in"
      @click.self="activeModalItem = null"
    >
      <div class="relative w-full max-w-2xl max-h-[90vh] overflow-y-auto rounded-3xl glass-card bg-white dark:bg-space-950 border border-pink-200 dark:border-white/10 p-6 sm:p-8 shadow-2xl space-y-6">
        <!-- 关闭按钮 -->
        <button
          @click="activeModalItem = null"
          class="absolute top-5 right-5 p-2 rounded-full text-slate-400 hover:text-slate-700 dark:hover:text-white bg-slate-100 dark:bg-white/5 transition-all"
        >
          <X class="w-5 h-5" />
        </button>

        <!-- 头部信息 -->
        <div class="flex flex-col sm:flex-row gap-5 items-center sm:items-start text-center sm:text-left">
          <div class="w-28 sm:w-36 aspect-[3/4] rounded-2xl overflow-hidden shadow-xl shrink-0 bg-slate-900">
            <img
              :src="activeModalItem.coverUrl"
              :alt="activeModalItem.nameCn || activeModalItem.name"
              referrerpolicy="no-referrer"
              class="w-full h-full object-cover"
            />
          </div>

          <div class="flex-1 space-y-2.5">
            <div class="flex flex-wrap items-center justify-center sm:justify-start gap-2">
              <span class="px-2.5 py-0.5 rounded-full text-xs font-bold bg-pink-500 text-white">
                {{ activeModalItem.subjectTypeName }}
              </span>
              <span class="px-2 py-0.5 rounded-full text-xs font-medium bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 border border-emerald-500/20">
                {{ activeModalItem.collectionTypeName }}
              </span>
              <span v-if="activeModalItem.rankNum" class="px-2 py-0.5 rounded-full text-xs font-bold bg-amber-400 text-slate-900">
                Rank #{{ activeModalItem.rankNum }}
              </span>
            </div>

            <h2 class="text-xl sm:text-2xl font-black text-slate-900 dark:text-slate-100">
              {{ activeModalItem.nameCn || activeModalItem.name }}
            </h2>
            <p v-if="activeModalItem.name !== activeModalItem.nameCn" class="text-xs text-slate-400 font-mono">
              {{ activeModalItem.name }}
            </p>

            <!-- 评分展示 -->
            <div class="flex flex-wrap items-center justify-center sm:justify-start gap-3 pt-1">
              <div class="flex items-center space-x-1.5 px-3 py-1 rounded-xl bg-pink-500/10 text-pink-600 dark:text-pink-400 font-bold text-sm">
                <Star class="w-4 h-4 fill-amber-400 text-amber-400" />
                <span>我的评分: {{ activeModalItem.userRate ? activeModalItem.userRate + ' / 10' : '未评' }}</span>
              </div>
              <div class="flex items-center space-x-1 px-2.5 py-1 rounded-xl bg-slate-100 dark:bg-white/5 text-slate-700 dark:text-slate-300 text-xs font-semibold">
                <span>Bangumi 全站: ★ {{ activeModalItem.score || '暂无' }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 用户的短评与吐槽 (详情展示) -->
        <div v-if="activeModalItem.userComment" class="p-4 rounded-2xl bg-pink-50/80 dark:bg-white/[0.03] border border-pink-200/60 dark:border-white/10 space-y-2">
          <div class="flex items-center space-x-2 text-xs font-bold text-pink-600 dark:text-pink-400">
            <MessageSquare class="w-4 h-4" />
            <span>站长主观评语 / 吐槽：</span>
          </div>
          <p class="text-xs sm:text-sm text-slate-700 dark:text-slate-300 leading-relaxed whitespace-pre-wrap font-sans">
            {{ activeModalItem.userComment }}
          </p>
        </div>

        <!-- 简介 -->
        <div v-if="activeModalItem.shortSummary" class="space-y-1.5">
          <h4 class="text-xs font-bold text-slate-400 uppercase tracking-wider">作品简介</h4>
          <p class="text-xs text-slate-600 dark:text-slate-400 leading-relaxed whitespace-pre-wrap">
            {{ activeModalItem.shortSummary }}
          </p>
        </div>

        <!-- 标签 Tags -->
        <div v-if="activeModalItem.tags" class="space-y-1.5">
          <h4 class="text-xs font-bold text-slate-400 uppercase tracking-wider">条目标签</h4>
          <div class="flex flex-wrap gap-1.5">
            <span
              v-for="tag in activeModalItem.tags.split(',')"
              :key="tag"
              class="px-2 py-0.5 rounded-lg text-[11px] bg-slate-100 dark:bg-white/5 text-slate-600 dark:text-slate-300"
            >
              #{{ tag.trim() }}
            </span>
          </div>
        </div>

        <!-- 弹窗底部操作 -->
        <div class="pt-4 border-t border-slate-100 dark:border-white/10 flex items-center justify-between">
          <div class="text-[11px] text-slate-400">
            放送/发行时间: {{ activeModalItem.airDate || '-' }}
            <span v-if="activeModalItem.eps"> • 共 {{ activeModalItem.eps }} 话</span>
          </div>
          <a
            :href="`https://bgm.tv/subject/${activeModalItem.subjectId}`"
            target="_blank"
            rel="noopener noreferrer"
            class="inline-flex items-center space-x-1.5 px-4 py-2 rounded-xl text-xs font-bold bg-pink-500 hover:bg-pink-600 text-white transition-colors"
          >
            <span>在 Bangumi 打开</span>
            <ExternalLink class="w-3.5 h-3.5" />
          </a>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import {
  Star, Trophy, Quote, Search, Filter,
  ExternalLink, Sparkles, ChevronDown, X, Clapperboard, Calendar, MessageSquare
} from 'lucide-vue-next';
import { getBangumiCollections, getBangumiSummary } from '@/api/bangumi';
import type { BangumiCollectionItem, BangumiSummary, BangumiProfile } from '@/types';

const collections = ref<BangumiCollectionItem[]>([]);
const summary = ref<Partial<BangumiSummary>>({
  totalCount: 0,
  avgScore: 0,
  avgUserRate: 0,
  commentCount: 0,
  lastUpdated: '-'
});
const profile = ref<Partial<BangumiProfile>>({
  nickname: 'NightMare丶明眸',
  id: 978388,
  sign: '纯主观打分，别骂我QAQ',
  url: 'https://bgm.tv/user/978388'
});

const loading = ref(true);
const keyword = ref('');
const selectedSubjectType = ref<number>(0); // 0 为全部
const selectedCollectionType = ref<number>(0); // 0 为全部
const sortRule = ref('user_rate_desc');
const activeModalItem = ref<BangumiCollectionItem | null>(null);

interface FilterOption {
  label: string;
  value: number;
  count?: number;
}

// 类型选项
const subjectTypeOptions = computed<FilterOption[]>(() => {
  const options: FilterOption[] = [{ label: '全部作品', value: 0, count: summary.value.totalCount }];
  if (summary.value.typeStats) {
    summary.value.typeStats.forEach(ts => {
      options.push({
        label: ts.name,
        value: ts.typeId || (ts.name === '动画' ? 2 : ts.name === '游戏' ? 4 : 1),
        count: ts.count
      });
    });
  }
  return options;
});

// 收藏类型选项
const collectionTypeOptions = computed<FilterOption[]>(() => {
  const options: FilterOption[] = [{ label: '全部状态', value: 0 }];
  if (summary.value.collectionTypeStats) {
    summary.value.collectionTypeStats.forEach(cs => {
      options.push({
        label: cs.name,
        value: cs.typeId || 2,
        count: cs.count
      });
    });
  }
  return options;
});

// 过滤后的列表
const filteredCollections = computed(() => {
  return collections.value.filter(item => {
    // 条目类型筛选
    if (selectedSubjectType.value > 0 && item.subjectType !== selectedSubjectType.value) {
      return false;
    }
    // 收藏状态筛选
    if (selectedCollectionType.value > 0 && item.collectionType !== selectedCollectionType.value) {
      return false;
    }
    // 关键词搜索
    if (keyword.value.trim()) {
      const q = keyword.value.trim().toLowerCase();
      const inName = item.name && item.name.toLowerCase().includes(q);
      const inNameCn = item.nameCn && item.nameCn.toLowerCase().includes(q);
      const inComment = item.userComment && item.userComment.toLowerCase().includes(q);
      const inTags = item.tags && item.tags.toLowerCase().includes(q);
      if (!inName && !inNameCn && !inComment && !inTags) {
        return false;
      }
    }
    return true;
  });
});

const loadData = async () => {
  loading.value = true;
  try {
    const [summaryRes, listRes] = await Promise.all([
      getBangumiSummary(),
      getBangumiCollections({ sort: sortRule.value })
    ]);
    if (summaryRes) {
      summary.value = summaryRes;
      if (summaryRes.profile) {
        profile.value = summaryRes.profile;
      }
    }
    collections.value = listRes || [];
  } catch (error) {
    console.error('获取 Bangumi 数据失败:', error);
  } finally {
    loading.value = false;
  }
};

const handleSortChange = async () => {
  try {
    const listRes = await getBangumiCollections({ sort: sortRule.value });
    collections.value = listRes || [];
  } catch (error) {
    console.error('更新排序失败:', error);
  }
};

// 监听排序变化
import { watch } from 'vue';
watch(sortRule, () => {
  handleSortChange();
});

const resetFilters = () => {
  selectedSubjectType.value = 0;
  selectedCollectionType.value = 0;
  keyword.value = '';
};

const openModal = (item: BangumiCollectionItem) => {
  activeModalItem.value = item;
};

const onImageError = (e: Event) => {
  const target = e.target as HTMLImageElement;
  target.src = '/images/bangumi-placeholder.webp';
};

onMounted(() => {
  loadData();
});
</script>
