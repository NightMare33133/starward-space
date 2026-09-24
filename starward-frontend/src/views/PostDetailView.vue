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

    <!-- 文章正文区域 -->
    <article v-else-if="post" class="space-y-8">
      <!-- 头部元信息区 -->
      <header class="space-y-4 border-b border-white/10 pb-8">
        <!-- 标签集合 -->
        <div class="flex items-center gap-2 flex-wrap">
          <span
            v-for="tag in post.tags"
            :key="tag.id"
            class="px-2.5 py-0.5 rounded-full text-xs font-medium bg-nebula-cyan/10 text-nebula-cyan border border-nebula-cyan/20"
          >
            # {{ tag.name }}
          </span>
        </div>

        <!-- 文章标题 -->
        <h1 class="text-2xl sm:text-4xl font-extrabold text-white tracking-tight leading-tight">
          {{ post.title }}
        </h1>

        <!-- 发布信息、阅读量、字数估算 -->
        <div class="flex flex-wrap items-center gap-4 text-xs text-slate-400 font-mono pt-2">
          <span class="flex items-center space-x-1.5">
            <Calendar class="w-4 h-4 text-slate-500" />
            <span>{{ formatDate(post.publishedAt) }}</span>
          </span>
          <span class="flex items-center space-x-1.5">
            <Eye class="w-4 h-4 text-slate-500" />
            <span>{{ post.viewCount }} 次阅读</span>
          </span>
          <span class="flex items-center space-x-1.5">
            <Clock class="w-4 h-4 text-slate-500" />
            <span>约 {{ Math.ceil(post.content.length / 400) }} 分钟阅读</span>
          </span>
        </div>
      </header>

      <!-- Markdown 沉浸式阅读器 -->
      <div class="glass-card rounded-3xl p-6 sm:p-10 shadow-2xl border border-white/10">
        <MarkdownViewer :content="post.content" />
      </div>

      <!-- 文末声明与分享 -->
      <div class="glass-card rounded-2xl p-6 border border-white/10 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs text-slate-400">
        <div class="space-y-1 text-center sm:text-left">
          <p class="text-slate-300 font-medium">版权与许可</p>
          <p>本文由 NightMare33133 原创，采用 CC BY-NC-SA 4.0 国际许可协议。</p>
        </div>
        <button
          @click="copyArticleUrl"
          class="px-4 py-2 rounded-xl bg-white/5 hover:bg-white/10 text-slate-300 hover:text-white transition-colors flex items-center space-x-2 shrink-0"
        >
          <Share2 class="w-3.5 h-3.5" />
          <span>{{ copied ? '链接已复制！' : '分享此文' }}</span>
        </button>
      </div>
    </article>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
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
