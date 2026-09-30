<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 py-10 space-y-8">
    <!-- 返回按钮 -->
    <div>
      <router-link
        to="/posts"
        class="inline-flex items-center space-x-2 text-xs text-slate-400 hover:text-nebula-cyan transition-colors"
      >
        <ArrowLeft class="w-4 h-4" />
        <span>返回文章列表</span>
      </router-link>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="text-center py-20 text-slate-500 text-sm">
      <div class="inline-block w-8 h-8 border-2 border-nebula-cyan border-t-transparent rounded-full animate-spin mb-3"></div>
      <p>正在解密星际文档正文...</p>
    </div>

    <!-- 错误处理 -->
    <div v-else-if="error" class="text-center py-20 glass-card rounded-2xl p-8 space-y-4">
      <div class="text-rose-400 font-mono text-sm">{{ error }}</div>
      <router-link to="/posts" class="text-xs text-nebula-cyan underline">返回全部文章</router-link>
    </div>

    <!-- 草稿预览专属提示横幅（仅管理员可见） -->
    <div
      v-if="post && (post.status || '').toUpperCase() === 'DRAFT'"
      class="px-4 py-3 rounded-2xl bg-amber-500/10 border border-amber-500/30 text-amber-700 dark:text-amber-300 text-xs flex items-center justify-between shadow-sm backdrop-blur-md"
    >
      <div class="flex items-center space-x-2">
        <span class="inline-block w-2 h-2 rounded-full bg-amber-400 animate-pulse"></span>
        <span class="font-semibold">⚡ 当前为草稿预览模式</span>
        <span class="text-amber-600/80 dark:text-amber-400/80 hidden sm:inline">（仅列车长登录状态可见，尚未对全宇宙公开发布）</span>
      </div>
      <router-link
        :to="`/admin/posts/edit/${post.id}`"
        class="px-3 py-1 rounded-lg bg-amber-500/20 hover:bg-amber-500/30 font-medium transition-colors"
      >
        进入编辑 ✏️
      </router-link>
    </div>

    <!-- 文章一体化阅读卡片 (Unified Magazine Reading Card) -->
    <article v-else-if="post" class="glass-card rounded-3xl p-6 sm:p-10 shadow-2xl border border-pink-200/50 dark:border-white/10 space-y-6 sm:space-y-8">
      <!-- 头部元信息区 -->
      <header class="space-y-4 border-b border-pink-200/40 dark:border-white/10 pb-6">
        <!-- 标签集合 -->
        <div class="flex items-center gap-2 flex-wrap">
          <span
            v-for="tag in post.tags"
            :key="tag.id"
            class="px-2.5 py-0.5 rounded-full text-xs font-medium bg-pink-100/80 text-pink-700 border border-pink-200 dark:bg-nebula-cyan/10 dark:text-nebula-cyan dark:border-nebula-cyan/20"
          >
            # {{ tag.name }}
          </span>
        </div>

        <!-- 文章标题 -->
        <h1 class="text-2xl sm:text-4xl font-extrabold text-slate-800 dark:text-white tracking-tight leading-tight">
          {{ post.title }}
        </h1>

        <!-- 发布信息、阅读量、字数估算 -->
        <div class="flex flex-wrap items-center gap-4 text-xs text-slate-500 dark:text-slate-400 font-mono pt-1">
          <span class="flex items-center space-x-1.5">
            <Calendar class="w-4 h-4 text-slate-400 dark:text-slate-500" />
            <span>{{ formatDate(postDate) }}</span>
          </span>
          <span class="flex items-center space-x-1.5">
            <Eye class="w-4 h-4 text-slate-400 dark:text-slate-500" />
            <span>{{ post.viewCount }} 次阅读</span>
          </span>
          <span class="flex items-center space-x-1.5">
            <Clock class="w-4 h-4 text-slate-400 dark:text-slate-500" />
            <span>约 {{ Math.max(1, Math.ceil(postContent.length / 400)) }} 分钟阅读</span>
          </span>
        </div>
      </header>

      <!-- 文章内嵌精选封面插画展台 (智能自适应：横图宽屏，竖图居中垂直原画展台) -->
      <div v-if="postCover" class="space-y-2">
        <div class="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 px-1 font-mono">
          <span class="flex items-center space-x-1.5 flex-wrap">
            <Sparkles class="w-3.5 h-3.5 text-pink-500 dark:text-nebula-cyan" />
            <span>精选文章封面 · {{ isVerticalCover ? '竖版插画完整原画' : '宽屏全景画框' }}</span>
            <span
              v-if="post.rawCoverImage"
              class="text-[10px] px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 dark:bg-emerald-950/60 dark:text-emerald-300 font-mono"
            >
              无损原画展台
            </span>
          </span>

          <div class="flex items-center space-x-2">
            <!-- 模式切换开关 -->
            <button
              type="button"
              @click="toggleCoverMode"
              class="px-2.5 py-1 rounded-lg glass-card border border-pink-200/60 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 text-[11px] text-slate-600 dark:text-slate-300 transition-colors flex items-center space-x-1 shadow-sm"
              :title="coverDisplayMode === 'banner' ? '切换为自适应无损原画完整展示' : '切换为宽屏横条画框'"
            >
              <SlidersHorizontal class="w-3 h-3 text-pink-500 dark:text-nebula-cyan" />
              <span>{{ coverDisplayMode === 'banner' ? '自适应完整原图' : '宽屏切片视角' }}</span>
            </button>

            <!-- 放大灯箱按钮 -->
            <button
              type="button"
              @click="showLightbox = true"
              class="p-1 rounded-lg glass-card border border-pink-200/60 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
              title="全屏查看高清原图"
            >
              <Maximize2 class="w-3.5 h-3.5" />
            </button>
          </div>
        </div>

        <!-- 展台主体容器 -->
        <!-- 模式 A：自适应原图展示 (竖图竖放居中精致展台，横图舒展铺满) -->
        <div
          v-if="coverDisplayMode === 'natural'"
          class="relative group rounded-3xl overflow-hidden shadow-xl border border-pink-200/50 dark:border-white/10 p-2 sm:p-4 bg-slate-900/5 dark:bg-space-950/40"
          :class="isVerticalCover ? 'max-w-md md:max-w-lg mx-auto' : 'w-full'"
        >
          <!-- 竖图专属：背后柔和星辉环境漫反射光晕 -->
          <div
            v-if="isVerticalCover"
            class="absolute inset-0 -z-10 scale-105 filter blur-3xl opacity-25 dark:opacity-35 bg-cover bg-center pointer-events-none transition-opacity duration-700"
            :style="{ backgroundImage: `url(${postCover})` }"
          ></div>

          <div class="rounded-2xl overflow-hidden relative cursor-zoom-in" @click="showLightbox = true">
            <img
              :src="postCover"
              :alt="post.title"
              decoding="async"
              @load="handleCoverLoad"
              class="w-full h-auto max-h-[720px] object-contain mx-auto block group-hover:scale-101 transition-transform duration-500"
            />
            <div class="absolute inset-0 bg-black/35 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center text-white text-xs space-x-1.5 backdrop-blur-[2px]">
              <Maximize2 class="w-4 h-4" />
              <span>点击查看高清无损原图</span>
            </div>
          </div>
        </div>

        <!-- 模式 B：宽屏画框全景模式 (固定横向视口) -->
        <div
          v-else
          class="w-full rounded-2xl overflow-hidden shadow-lg border border-pink-200/40 dark:border-white/10 max-h-[460px] bg-slate-900/10 dark:bg-space-950/60 relative group cursor-zoom-in"
          @click="showLightbox = true"
        >
          <img
            :src="displayCoverSrc"
            :alt="post.title"
            decoding="async"
            @load="handleCoverLoad"
            class="w-full h-full max-h-[460px] object-cover object-[center_20%] group-hover:scale-102 transition-transform duration-700 ease-out"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-slate-950/30 via-transparent to-transparent opacity-20"></div>
          <div class="absolute bottom-3 right-3 px-2.5 py-1 rounded-lg bg-black/60 text-white text-[11px] opacity-0 group-hover:opacity-100 transition-opacity flex items-center space-x-1 backdrop-blur-md">
            <Maximize2 class="w-3.5 h-3.5" />
            <span>点击查看原图</span>
          </div>
        </div>
      </div>

      <!-- Markdown 沉浸式阅读器 -->
      <div class="pt-2">
        <MarkdownViewer :content="postContent" />
      </div>

      <!-- 文末声明与分享 -->
      <div class="pt-6 border-t border-pink-200/40 dark:border-white/10 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-slate-500 dark:text-slate-400">
        <div class="space-y-1 text-center sm:text-left">
          <p class="text-slate-700 dark:text-slate-300 font-medium">版权与许可</p>
          <p>本文由 NightMare33133 原创，采用 CC BY-NC-SA 4.0 国际许可协议。</p>
        </div>
        <button
          @click="copyArticleUrl"
          class="px-4 py-2 rounded-xl bg-pink-100/70 hover:bg-pink-100 text-slate-700 dark:bg-white/5 dark:hover:bg-white/10 dark:text-slate-300 dark:hover:text-white transition-colors flex items-center space-x-2 shrink-0 border border-pink-200/60 dark:border-white/10 shadow-sm"
        >
          <Share2 class="w-3.5 h-3.5" />
          <span>{{ copied ? '链接已复制！' : '分享此文' }}</span>
        </button>
      </div>
    </article>

    <!-- 高清大图全屏画廊灯箱 (Lightbox - 传送至 body 顶层并设置最高 z-[100]，彻底解决导航栏与音乐条层级遮挡) -->
    <Teleport to="body">
      <div
        v-if="showLightbox"
        class="fixed inset-0 z-[100] flex items-center justify-center p-4 sm:p-8 bg-slate-950/92 backdrop-blur-xl animate-fade-in"
        @click="showLightbox = false"
      >
        <!-- 屏幕右上角常驻独立关闭按钮 -->
        <button
          type="button"
          @click="showLightbox = false"
          class="fixed top-5 right-5 sm:top-8 sm:right-8 z-[110] p-2.5 sm:p-3 rounded-full bg-white/15 hover:bg-white/25 text-white/90 hover:text-white backdrop-blur-xl border border-white/20 shadow-2xl transition-all hover:scale-110 active:scale-95 cursor-pointer"
          title="关闭全屏预览 (Esc)"
        >
          <X class="w-5 h-5 sm:w-6 sm:h-6" />
        </button>

        <!-- 图像主体与元信息底栏 -->
        <div class="relative max-w-6xl max-h-[92vh] flex flex-col items-center select-none" @click.stop>
          <img
            :src="postCover"
            :alt="post?.title"
            decoding="async"
            class="max-w-full max-h-[78vh] sm:max-h-[82vh] rounded-2xl object-contain shadow-2xl border border-white/15 cursor-zoom-out"
            @click="showLightbox = false"
          />

          <div class="mt-4 flex flex-wrap items-center justify-center gap-3 text-xs font-mono text-white/85">
            <span v-if="naturalW && naturalH" class="px-3 py-1 rounded-full bg-white/10 border border-white/15 backdrop-blur-md">
              尺寸: {{ naturalW }} × {{ naturalH }} px ({{ isVerticalCover ? '竖版插画' : '横版宽屏' }})
            </span>
            <a
              :href="postCover"
              target="_blank"
              class="px-3.5 py-1 rounded-full bg-pink-500/20 hover:bg-pink-500/30 text-pink-300 hover:text-pink-200 border border-pink-400/30 backdrop-blur-md transition-colors flex items-center space-x-1.5"
            >
              <ExternalLink class="w-3.5 h-3.5" />
              <span>新标签页查看原始文件</span>
            </a>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import {
  ArrowLeft,
  Calendar,
  Eye,
  Clock,
  Share2,
  Maximize2,
  SlidersHorizontal,
  Sparkles,
  X,
  ExternalLink
} from 'lucide-vue-next';
import { getPostDetail } from '@/api/posts';
import MarkdownViewer from '@/components/MarkdownViewer.vue';
import type { PostDetailVO } from '@/types';

const route = useRoute();
const post = ref<PostDetailVO | null>(null);
const loading = ref(true);
const error = ref('');
const copied = ref(false);

const postContent = computed(() => post.value?.content || post.value?.contentMd || '');
const postDate = computed(() => post.value?.publishedAt || post.value?.createdAt || '');

const defaultCovers = [
  '/images/hsr/feiying_sakura.webp',    // 绯英·落樱晨曦 (16:9 横版插画)
  '/images/hsr/firefly_night.webp',     // 流萤·深空之夜 (16:9 横版插画)
  '/images/hsr/astral_express_bg.webp', // 星穹列车·站台 (16:9 银河风景)
  'https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=1200&q=80',
  'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=1200&q=80',
];

const postCover = computed(() => {
  if (!post.value) return '';
  // 详情页优先展示未经裁切的无损原画（竖版立绘/全身插画）
  if (post.value.rawCoverImage) return post.value.rawCoverImage;
  if (post.value.coverImage) return post.value.coverImage;
  const idx = Math.abs(Number(post.value.id) || 0) % defaultCovers.length;
  return defaultCovers[idx];
});

const displayCoverSrc = computed(() => {
  if (!post.value) return postCover.value;
  // 当用户在详情页特意切换为“宽屏画框”模式时，若存在 16:10 裁切卡片封面则直接展现 16:10 封面
  if (coverDisplayMode.value === 'banner' && post.value.coverImage) {
    return post.value.coverImage;
  }
  return postCover.value;
});

// 封面展台展示模式与智能比例自适应
const isVerticalCover = ref(false);
const coverDisplayMode = ref<'natural' | 'banner'>('natural');
const showLightbox = ref(false);
const naturalW = ref(0);
const naturalH = ref(0);

const handleCoverLoad = (e: Event) => {
  const img = e.target as HTMLImageElement;
  if (!img) return;
  naturalW.value = img.naturalWidth;
  naturalH.value = img.naturalHeight;
  // 如果高度大于宽度的 1.05 倍，判断为竖版立绘/插画
  if (img.naturalHeight > img.naturalWidth * 1.05) {
    isVerticalCover.value = true;
    coverDisplayMode.value = 'natural';
  } else {
    isVerticalCover.value = false;
  }
};

const toggleCoverMode = () => {
  coverDisplayMode.value = coverDisplayMode.value === 'banner' ? 'natural' : 'banner';
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日`;
};

const copyArticleUrl = () => {
  navigator.clipboard.writeText(window.location.href);
  copied.value = true;
  setTimeout(() => {
    copied.value = false;
  }, 2000);
};

onMounted(async () => {
  window.addEventListener('keydown', handleGlobalKeydown);
  const id = Number(route.params.id);
  if (!id) {
    error.value = '文章 ID 不存在';
    loading.value = false;
    return;
  }
  try {
    post.value = await getPostDetail(id);
  } catch (err: any) {
    error.value = err.message || '获取文章详情失败';
  } finally {
    loading.value = false;
  }
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleGlobalKeydown);
  document.body.style.overflow = '';
});

// 监听大图画廊开关：锁定背景滚动与支持 ESC 关闭
watch(showLightbox, (open) => {
  if (open) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = '';
  }
});

const handleGlobalKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && showLightbox.value) {
    showLightbox.value = false;
  }
};
</script>
