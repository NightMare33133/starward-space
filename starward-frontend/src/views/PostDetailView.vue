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

      <!-- 文章内嵌精选封面插画展台 (融合一体化设计，参考 Rainzt / CuteLeaf) -->
      <div
        v-if="postCover"
        class="w-full rounded-2xl overflow-hidden shadow-lg border border-pink-200/40 dark:border-white/10 max-h-[420px] bg-slate-900/10 dark:bg-space-950/60 relative group"
      >
        <img
          :src="postCover"
          :alt="post.title"
          class="w-full h-full max-h-[420px] object-cover object-[center_20%] group-hover:scale-102 transition-transform duration-700 ease-out"
        />
        <div class="absolute inset-0 bg-gradient-to-t from-slate-950/30 via-transparent to-transparent opacity-20"></div>
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
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { ArrowLeft, Calendar, Eye, Clock, Share2 } from 'lucide-vue-next';
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
  '/images/hsr/feiying_sakura.png',    // 绯英·落樱晨曦 (16:9 横版插画)
  '/images/hsr/firefly_night.jpg',     // 流萤·深空之夜 (16:9 横版插画)
  '/images/hsr/astral_express_bg.jpg', // 星穹列车·站台 (16:9 银河风景)
  'https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=1200&q=80',
  'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=1200&q=80',
];

const postCover = computed(() => {
  if (!post.value) return '';
  if (post.value.coverImage) return post.value.coverImage;
  const idx = Math.abs(Number(post.value.id) || 0) % defaultCovers.length;
  return defaultCovers[idx];
});

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
</script>
