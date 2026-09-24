<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 py-10 space-y-8">
    <!-- 头部标题区 -->
    <div class="space-y-3 border-b border-white/10 pb-6">
      <h1 class="text-3xl font-extrabold text-white tracking-tight flex items-center space-x-3">
        <BookOpen class="w-8 h-8 text-nebula-cyan" />
        <span>文章归档 · Articles</span>
      </h1>
      <p class="text-sm text-slate-400">
        记录软件工程思维、全栈架构、AI Agent 实践以及求学探索的技术札记。
      </p>
    </div>

    <!-- 搜索与标签过滤栏 -->
    <div class="space-y-4">
      <!-- 搜索框 -->
      <div class="relative">
        <Search class="w-4 h-4 text-slate-500 absolute left-4 top-1/2 -translate-y-1/2" />
        <input
          v-model="searchQuery"
          type="text"
          placeholder="搜索文章标题或摘要关键词..."
          class="w-full pl-11 pr-4 py-2.5 rounded-2xl glass-card bg-space-950/60 border border-white/10 text-slate-200 placeholder-slate-500 text-sm focus:outline-none focus:border-nebula-cyan/50 transition-colors"
        />
      </div>

      <!-- 标签 Pills 筛选器 -->
      <div class="flex items-center gap-2 flex-wrap">
        <button
          @click="selectedTag = ''"
          class="px-3.5 py-1 rounded-full text-xs font-medium transition-all"
          :class="selectedTag === '' ? 'bg-nebula-cyan text-space-950 font-bold shadow-md shadow-nebula-cyan/20' : 'glass-card text-slate-400 hover:text-slate-200'"
        >
          全部 ({{ posts.length }})
        </button>
        <button
          v-for="tag in allTags"
          :key="tag"
          @click="selectedTag = tag"
          class="px-3.5 py-1 rounded-full text-xs font-medium transition-all"
          :class="selectedTag === tag ? 'bg-nebula-cyan text-space-950 font-bold shadow-md shadow-nebula-cyan/20' : 'glass-card text-slate-400 hover:text-slate-200'"
        >
          # {{ tag }}
        </button>
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="text-center py-16 text-slate-500 text-sm">
      <div class="inline-block w-6 h-6 border-2 border-nebula-cyan border-t-transparent rounded-full animate-spin mb-2"></div>
      <p>正在读取星向知识库...</p>
    </div>

    <!-- 文章列表 -->
    <div v-else-if="filteredPosts.length > 0" class="space-y-4">
      <article
        v-for="post in filteredPosts"
        :key="post.id"
        @click="$router.push(`/posts/${post.id}`)"
        class="glass-card rounded-2xl p-6 group cursor-pointer transition-all duration-300 hover:border-nebula-cyan/40"
      >
        <div class="flex flex-col sm:flex-row sm:items-baseline justify-between gap-2 mb-3">
          <h2 class="text-lg font-bold text-white group-hover:text-nebula-cyan transition-colors">
            {{ post.title }}
          </h2>
          <span class="text-xs text-slate-500 font-mono shrink-0">
            {{ formatDate(post.publishedAt) }}
          </span>
        </div>

        <p class="text-xs text-slate-400 leading-relaxed mb-4 line-clamp-2">
          {{ post.summary }}
        </p>

        <div class="flex items-center justify-between text-xs text-slate-500">
          <div class="flex items-center gap-2 flex-wrap">
            <span
              v-for="tag in post.tags"
              :key="tag.id"
              class="px-2 py-0.5 rounded-full text-[11px] bg-white/5 text-slate-300 border border-white/10"
            >
              # {{ tag.name }}
            </span>
          </div>
          <span class="flex items-center space-x-1 font-mono">
            <Eye class="w-3.5 h-3.5" />
            <span>{{ post.viewCount }} 阅读</span>
          </span>
        </div>
      </article>
    </div>

    <!-- 无搜索结果 -->
    <div v-else class="text-center py-16 text-slate-500 text-sm glass-card rounded-2xl">
      <p>未找到符合条件的文章 🚀</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import { BookOpen, Search, Eye } from 'lucide-vue-next';
import { getPostList } from '@/api/posts';
import type { PostListVO } from '@/types';

const posts = ref<PostListVO[]>([]);
const loading = ref(true);
const searchQuery = ref('');
const selectedTag = ref('');

const allTags = computed(() => {
  const tagsSet = new Set<string>();
  posts.value.forEach(p => {
    p.tags?.forEach(t => tagsSet.add(t.name));
  });
  return Array.from(tagsSet);
});

const filteredPosts = computed(() => {
  return posts.value.filter(p => {
    const matchesSearch = !searchQuery.value ||
      p.title.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      p.summary.toLowerCase().includes(searchQuery.value.toLowerCase());
    
    const matchesTag = !selectedTag.value ||
      p.tags?.some(t => t.name === selectedTag.value);

    return matchesSearch && matchesTag;
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
