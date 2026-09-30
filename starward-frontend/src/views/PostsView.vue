<template>
  <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
    <!-- 头部标题区 -->
    <div class="flex flex-col md:flex-row md:items-end justify-between gap-4 border-b border-pink-200/50 dark:border-white/10 pb-6">
      <div class="space-y-2">
        <h1 class="text-3xl sm:text-4xl font-extrabold text-slate-800 dark:text-white tracking-tight flex items-center space-x-3">
          <BookOpen class="w-8 h-8 text-pink-500 dark:text-nebula-cyan" />
          <span>文章归档 · Articles</span>
        </h1>
        <p class="text-sm text-slate-600 dark:text-slate-400">
          记录软件工程思维、全栈架构、AI Agent 实践以及求学探索的技术札记。
        </p>
      </div>

      <div class="flex items-center space-x-3 text-xs font-mono text-slate-500 dark:text-slate-400 shrink-0">
        <span class="px-3 py-1 rounded-full glass-card border border-pink-200/60 dark:border-white/10">
          共 <strong class="text-pink-600 dark:text-nebula-cyan">{{ posts.length }}</strong> 篇记录
        </span>
        <span class="px-3 py-1 rounded-full glass-card border border-pink-200/60 dark:border-white/10">
          <strong class="text-pink-600 dark:text-nebula-cyan">{{ allTags.length }}</strong> 个航标
        </span>
      </div>
    </div>

    <!-- 搜索与标签过滤栏 -->
    <div class="space-y-4">
      <!-- 搜索框 -->
      <div class="relative max-w-2xl">
        <Search class="w-4 h-4 text-slate-400 dark:text-slate-500 absolute left-4 top-1/2 -translate-y-1/2" />
        <input
          v-model="searchQuery"
          type="text"
          placeholder="搜索文章标题、摘要或关键词..."
          class="w-full pl-11 pr-10 py-2.5 rounded-2xl glass-card bg-white/80 dark:bg-space-950/60 border border-pink-200/70 dark:border-white/10 text-slate-800 dark:text-slate-200 placeholder-slate-400 dark:placeholder-slate-500 text-sm focus:outline-none focus:border-pink-400 dark:focus:border-nebula-cyan/50 transition-colors shadow-sm"
        />
        <button
          v-if="searchQuery"
          @click="searchQuery = ''"
          class="absolute right-3.5 top-1/2 -translate-y-1/2 p-1 rounded-full hover:bg-slate-200 dark:hover:bg-white/10 text-slate-400 hover:text-slate-700 dark:hover:text-white transition-colors"
          title="清空搜索"
        >
          <X class="w-3.5 h-3.5" />
        </button>
      </div>

      <!-- 标签 Pills 筛选器 -->
      <div class="flex items-center gap-2 flex-wrap">
        <button
          @click="selectedTag = ''"
          class="px-3.5 py-1.5 rounded-full text-xs font-medium transition-all"
          :class="selectedTag === ''
            ? 'bg-gradient-to-r from-pink-500 to-rose-500 text-white dark:from-nebula-cyan dark:to-cyan-400 dark:text-space-950 font-bold shadow-md shadow-pink-500/20 dark:shadow-nebula-cyan/20 scale-105'
            : 'glass-card border border-pink-200/50 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'"
        >
          全部 ({{ posts.length }})
        </button>
        <button
          v-for="tag in allTags"
          :key="tag"
          @click="selectedTag = tag"
          class="px-3.5 py-1.5 rounded-full text-xs font-medium transition-all"
          :class="selectedTag === tag
            ? 'bg-gradient-to-r from-pink-500 to-rose-500 text-white dark:from-nebula-cyan dark:to-cyan-400 dark:text-space-950 font-bold shadow-md shadow-pink-500/20 dark:shadow-nebula-cyan/20 scale-105'
            : 'glass-card border border-pink-200/50 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-200'"
        >
          # {{ tag }} <span class="opacity-70 text-[10px]">({{ tagCounts[tag] || 0 }})</span>
        </button>
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="text-center py-24 text-slate-500 dark:text-slate-400 text-sm">
      <div class="inline-block w-8 h-8 border-2 border-pink-500 dark:border-nebula-cyan border-t-transparent rounded-full animate-spin mb-3"></div>
      <p class="font-mono">正在接入星向知识库...</p>
    </div>

    <!-- 文章卡片网格 -->
    <div v-else-if="filteredPosts.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 sm:gap-7">
      <article
        v-for="post in filteredPosts"
        :key="post.id"
        @click="$router.push(`/posts/${post.id}`)"
        class="glass-card rounded-3xl overflow-hidden group cursor-pointer border border-pink-200/60 dark:border-white/10 hover:border-pink-400/80 dark:hover:border-nebula-cyan/50 hover:shadow-xl hover:shadow-pink-400/10 dark:hover:shadow-nebula-cyan/15 hover:-translate-y-1.5 transition-all duration-300 flex flex-col justify-between"
      >
        <!-- 封面图容器 -->
        <div class="relative w-full aspect-[16/10] overflow-hidden bg-slate-900/5 dark:bg-space-900/60 select-none">
          <img
            :src="getPostCover(post)"
            :alt="post.title"
            loading="lazy"
            @error="handleImageError(post.id)"
            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700 ease-out"
          />
          <!-- 渐变遮罩增强文字对比 -->
          <div class="absolute inset-0 bg-gradient-to-t from-slate-950/70 via-slate-950/15 to-transparent opacity-50 group-hover:opacity-30 transition-opacity duration-300"></div>

          <!-- 左上角置顶胶囊 -->
          <div
            v-if="post.isPinned"
            class="absolute top-3 left-3 flex items-center space-x-1 px-2.5 py-1 rounded-full text-[11px] font-bold shadow-lg backdrop-blur-md bg-gradient-to-r from-amber-500 to-rose-500 text-white border border-amber-300/40"
          >
            <Bookmark class="w-3 h-3 fill-current" />
            <span>置顶推荐</span>
          </div>

          <!-- 右上角阅读预估胶囊 -->
          <div class="absolute top-3 right-3 px-2.5 py-1 rounded-full text-[10px] font-mono shadow-md backdrop-blur-md bg-black/45 text-white/90 border border-white/20 flex items-center space-x-1">
            <Clock class="w-3 h-3 text-pink-300 dark:text-nebula-cyan" />
            <span>约 {{ estimateReadTime(post.summary) }} 分钟</span>
          </div>

          <!-- 左下角发布日期胶囊 -->
          <div class="absolute bottom-3 left-3 px-2.5 py-0.5 rounded-lg text-[11px] font-mono shadow backdrop-blur-md bg-black/50 text-slate-200 border border-white/10 flex items-center space-x-1.5">
            <Calendar class="w-3 h-3 text-pink-300 dark:text-nebula-cyan" />
            <span>{{ formatDate(post.publishedAt || post.createdAt) }}</span>
          </div>
        </div>

        <!-- 内容区域 -->
        <div class="p-5 sm:p-6 flex-1 flex flex-col justify-between space-y-3">
          <div class="space-y-2">
            <!-- 标题 -->
            <h2 class="text-base sm:text-lg font-bold text-slate-800 dark:text-white group-hover:text-pink-600 dark:group-hover:text-nebula-cyan transition-colors line-clamp-2 leading-snug">
              {{ post.title }}
            </h2>

            <!-- 摘要 -->
            <p class="text-xs sm:text-sm text-slate-600 dark:text-slate-400 line-clamp-2 leading-relaxed">
              {{ post.summary || '暂无详细摘要，点击卡片探索更多星向记录...' }}
            </p>
          </div>

          <!-- 标签与底栏 -->
          <div class="space-y-3 pt-2">
            <!-- 标签 Pills -->
            <div class="flex items-center gap-1.5 flex-wrap">
              <span
                v-for="tag in post.tags"
                :key="tag.id"
                class="px-2.5 py-0.5 rounded-lg text-[11px] font-medium bg-pink-50 text-pink-700 border border-pink-200/60 dark:bg-white/5 dark:text-slate-300 dark:border-white/10 group-hover:border-pink-300 dark:group-hover:border-nebula-cyan/30 transition-colors"
              >
                # {{ tag.name }}
              </span>
            </div>

            <!-- 底栏数据与箭头 -->
            <div class="pt-3 border-t border-pink-200/40 dark:border-white/5 flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 font-mono">
              <span class="flex items-center space-x-1">
                <Eye class="w-3.5 h-3.5 text-slate-400 dark:text-slate-500" />
                <span>{{ post.viewCount || 0 }} 阅读</span>
              </span>

              <div class="flex items-center space-x-1 text-pink-600 dark:text-nebula-cyan text-xs font-semibold group-hover:translate-x-1 transition-transform">
                <span>阅读全文</span>
                <ArrowRight class="w-3.5 h-3.5" />
              </div>
            </div>
          </div>
        </div>
      </article>
    </div>

    <!-- 无搜索结果 -->
    <div v-else class="text-center py-20 text-slate-500 dark:text-slate-400 text-sm glass-card rounded-3xl border border-pink-200/50 dark:border-white/10 space-y-3">
      <p class="text-base font-semibold text-slate-700 dark:text-slate-300">未找到符合条件的文章 🚀</p>
      <p class="text-xs text-slate-500">试着换一个关键词，或者清空标签筛选看看吧</p>
      <button
        v-if="searchQuery || selectedTag"
        @click="searchQuery = ''; selectedTag = ''"
        class="px-4 py-1.5 rounded-xl bg-pink-500/10 text-pink-600 dark:bg-nebula-cyan/10 dark:text-nebula-cyan text-xs font-medium hover:opacity-80 transition-opacity"
      >
        重置所有筛选
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { BookOpen, Search, Eye, Calendar, Clock, Bookmark, ArrowRight, X } from 'lucide-vue-next';
import { getPostList } from '@/api/posts';
import type { PostListVO } from '@/types';

const posts = ref<PostListVO[]>([]);
const loading = ref(true);
const searchQuery = ref('');
const selectedTag = ref('');
const failedImages = ref<Record<number, boolean>>({});

// 星穹列车官方高清壁纸备选库（当文章未传 coverImage 或外部图片挂掉时均匀轮转兜底）
const defaultCovers = [
  '/images/hsr/himeko_express.png',
  '/images/hsr/march7th_selfie.png',
  '/images/hsr/danheng.png',
  '/images/hsr/welt.png',
  '/images/hsr/firefly_night.jpg',
  '/images/hsr/feiying_sakura.png',
  '/images/hsr/astral_express_bg.jpg'
];

const getPostCover = (post: PostListVO) => {
  if (post.coverImage && !failedImages.value[post.id]) {
    return post.coverImage;
  }
  const idx = Math.abs(Number(post.id) || 0) % defaultCovers.length;
  return defaultCovers[idx];
};

const handleImageError = (postId: number) => {
  failedImages.value[postId] = true;
};

const estimateReadTime = (summary?: string) => {
  const len = (summary || '').length;
  return Math.max(2, Math.min(15, Math.ceil(len / 35) + 2));
};

const allTags = computed(() => {
  const tagsSet = new Set<string>();
  posts.value.forEach(p => {
    p.tags?.forEach(t => tagsSet.add(t.name));
  });
  return Array.from(tagsSet);
});

const tagCounts = computed(() => {
  const counts: Record<string, number> = {};
  posts.value.forEach(p => {
    p.tags?.forEach(t => {
      counts[t.name] = (counts[t.name] || 0) + 1;
    });
  });
  return counts;
});

const filteredPosts = computed(() => {
  const q = searchQuery.value.trim().toLowerCase();
  const tag = selectedTag.value;

  return posts.value
    .filter(p => {
      const matchesSearch = !q ||
        p.title.toLowerCase().includes(q) ||
        (p.summary && p.summary.toLowerCase().includes(q));

      const matchesTag = !tag ||
        p.tags?.some(t => t.name === tag);

      return matchesSearch && matchesTag;
    })
    .sort((a, b) => {
      // 1. 置顶优先
      const pinA = a.isPinned ? 1 : 0;
      const pinB = b.isPinned ? 1 : 0;
      if (pinA !== pinB) return pinB - pinA;

      // 2. 发布日期/创建日期倒序
      const dateA = new Date(a.publishedAt || a.createdAt).getTime();
      const dateB = new Date(b.publishedAt || b.createdAt).getTime();
      return dateB - dateA;
    });
});

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
};

onMounted(async () => {
  try {
    posts.value = await getPostList();
  } catch (err) {
    console.error('Failed to load posts:', err);
  } finally {
    loading.value = false;
  }
});
</script>
