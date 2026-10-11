<template>
  <div class="space-y-6">
    <!-- 顶部数据看板 Metric Cards -->
    <div class="grid grid-cols-2 sm:grid-cols-4 gap-4">
      <div class="glass-card p-4 rounded-2xl border border-pink-200/50 dark:border-white/10 flex items-center space-x-3">
        <div class="p-2.5 rounded-xl bg-nebula-cyan/15 text-sky-600 dark:text-nebula-cyan border border-sky-400/30 dark:border-nebula-cyan/20">
          <BookOpen class="w-5 h-5" />
        </div>
        <div>
          <p class="text-xs text-slate-500 dark:text-slate-400">文章总数</p>
          <p class="text-xl font-bold text-slate-800 dark:text-white font-mono">{{ posts.length }}</p>
        </div>
      </div>

      <div class="glass-card p-4 rounded-2xl border border-pink-200/50 dark:border-white/10 flex items-center space-x-3">
        <div class="p-2.5 rounded-xl bg-emerald-500/15 text-emerald-600 dark:text-emerald-400 border border-emerald-500/30">
          <CheckCircle class="w-5 h-5" />
        </div>
        <div>
          <p class="text-xs text-slate-500 dark:text-slate-400">已发布</p>
          <p class="text-xl font-bold text-slate-800 dark:text-white font-mono">{{ publishedCount }}</p>
        </div>
      </div>

      <div class="glass-card p-4 rounded-2xl border border-pink-200/50 dark:border-white/10 flex items-center space-x-3">
        <div class="p-2.5 rounded-xl bg-amber-500/15 text-amber-600 dark:text-amber-400 border border-amber-500/30">
          <FileClock class="w-5 h-5" />
        </div>
        <div>
          <p class="text-xs text-slate-500 dark:text-slate-400">草稿箱</p>
          <p class="text-xl font-bold text-slate-800 dark:text-white font-mono">{{ draftCount }}</p>
        </div>
      </div>

      <div class="glass-card p-4 rounded-2xl border border-pink-200/50 dark:border-white/10 flex items-center space-x-3">
        <div class="p-2.5 rounded-xl bg-pink-500/15 text-pink-600 dark:text-nebula-pink border border-pink-400/30 dark:border-nebula-pink/20">
          <Eye class="w-5 h-5" />
        </div>
        <div>
          <p class="text-xs text-slate-500 dark:text-slate-400">累计阅读量</p>
          <p class="text-xl font-bold text-slate-800 dark:text-white font-mono">{{ totalViews }}</p>
        </div>
      </div>
    </div>

    <!-- 搜索筛选与操作栏 -->
    <div class="glass-card p-4 rounded-2xl border border-pink-200/50 dark:border-white/10 flex flex-col md:flex-row items-stretch md:items-center justify-between gap-3">
      <div class="flex flex-col sm:flex-row items-stretch sm:items-center gap-2.5 w-full md:w-auto">
        <!-- 搜索框 -->
        <div class="relative w-full sm:w-64">
          <Search class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-slate-400" />
          <input
            v-model="searchQuery"
            type="text"
            placeholder="搜索文章标题或 Slug..."
            class="w-full pl-9 pr-3 py-2 rounded-xl bg-white/80 dark:bg-space-950/70 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all font-mono shadow-inner"
          />
        </div>

        <!-- 状态过滤器 -->
        <div class="flex items-center space-x-1 p-1 rounded-xl bg-pink-100/60 dark:bg-space-950/80 border border-pink-200/60 dark:border-white/10 text-xs self-start sm:self-auto">
          <button
            @click="filterStatus = 'ALL'"
            class="px-2.5 py-1 rounded-lg transition-colors font-medium"
            :class="filterStatus === 'ALL' ? 'bg-amber-500/20 text-amber-700 dark:text-amber-300 font-bold' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          >
            全部
          </button>
          <button
            @click="filterStatus = 'PUBLISHED'"
            class="px-2.5 py-1 rounded-lg transition-colors font-medium"
            :class="filterStatus === 'PUBLISHED' ? 'bg-emerald-500/20 text-emerald-700 dark:text-emerald-300 font-bold' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          >
            已发布
          </button>
          <button
            @click="filterStatus = 'DRAFT'"
            class="px-2.5 py-1 rounded-lg transition-colors font-medium"
            :class="filterStatus === 'DRAFT' ? 'bg-amber-500/20 text-amber-700 dark:text-amber-300 font-bold' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
          >
            草稿
          </button>
        </div>
      </div>

      <div class="flex items-center space-x-3 w-full md:w-auto justify-end pt-1 md:pt-0 border-t md:border-t-0 border-pink-200/30 dark:border-white/5">
        <!-- 刷新按钮 -->
        <button
          @click="fetchPosts"
          :disabled="loading"
          class="p-2.5 rounded-xl border border-pink-200/60 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 bg-white/60 dark:bg-white/5 hover:bg-white/90 dark:hover:bg-white/10 text-slate-600 dark:text-slate-300 transition-colors shadow-sm"
          title="刷新列表"
        >
          <RefreshCw class="w-4 h-4" :class="{ 'animate-spin': loading }" />
        </button>

        <!-- 新建文章按钮 -->
        <router-link
          to="/admin/posts/new"
          class="flex-1 sm:flex-none justify-center px-4 py-2 rounded-xl bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan text-white text-xs font-bold shadow-lg shadow-amber-500/20 hover:opacity-95 transition-all flex items-center space-x-1.5 shrink-0"
        >
          <Plus class="w-4 h-4" />
          <span>撰写新文章</span>
        </router-link>
      </div>
    </div>

    <!-- 文章列表表格 -->
    <div class="glass-card rounded-2xl border border-pink-200/50 dark:border-white/10 overflow-hidden shadow-lg">
      <!-- 加载中 -->
      <div v-if="loading" class="py-16 text-center text-slate-500 dark:text-slate-400 flex flex-col items-center justify-center space-y-2">
        <Loader2 class="w-6 h-6 animate-spin text-amber-400" />
        <span class="text-xs font-mono">从星际数据库加载文章中...</span>
      </div>

      <!-- 空状态 -->
      <div v-else-if="filteredPosts.length === 0" class="py-16 text-center text-slate-500 dark:text-slate-400 space-y-2">
        <BookOpen class="w-8 h-8 mx-auto text-slate-400 dark:text-slate-600" />
        <p class="text-sm">暂未检索到符合条件的星向文章</p>
      </div>

      <!-- 内容展示区 (支持桌面端高密度表格 + 移动端自适应流式卡片) -->
      <div v-else>
        <!-- 1. 移动端：精致流式卡片列表 (Mobile Card View, md:hidden) -->
        <div class="md:hidden divide-y divide-pink-200/30 dark:divide-white/5">
          <div
            v-for="post in filteredPosts"
            :key="'mobile-' + post.id"
            class="p-4 space-y-3 hover:bg-pink-500/[0.03] dark:hover:bg-white/[0.02] transition-colors"
          >
            <!-- 卡片头部：ID + 状态快捷切换 + 置顶 + 阅读量与日期 -->
            <div class="flex items-center justify-between text-xs">
              <div class="flex items-center space-x-2">
                <span class="font-mono text-[11px] text-slate-400 dark:text-slate-500 font-bold bg-pink-100/60 dark:bg-white/5 px-1.5 py-0.5 rounded">
                  #{{ post.id }}
                </span>
                <!-- 发布/草稿状态切换 -->
                <button
                  @click="toggleStatus(post)"
                  class="px-2 py-0.5 rounded-full text-[10px] font-medium transition-all"
                  :class="isPublished(post) ? 'bg-emerald-500/15 text-emerald-700 dark:text-emerald-300 border border-emerald-500/30' : 'bg-amber-500/15 text-amber-700 dark:text-amber-300 border border-amber-500/30'"
                  :title="isPublished(post) ? '点击转为私密草稿' : '点击公开发布文章'"
                >
                  {{ isPublished(post) ? '● 已发布' : '○ 草稿箱' }}
                </button>
                <!-- 置顶切换 -->
                <button
                  @click="togglePin(post)"
                  class="p-1 rounded transition-colors"
                  :class="isPinned(post) ? 'text-amber-500 dark:text-amber-400' : 'text-slate-400 dark:text-slate-600'"
                  :title="isPinned(post) ? '取消置顶' : '置顶文章'"
                >
                  <Pin class="w-3.5 h-3.5" :class="{ 'fill-amber-400': isPinned(post) }" />
                </button>
              </div>

              <!-- 阅读量与时间 -->
              <div class="flex items-center space-x-1.5 text-[11px] font-mono text-slate-400 dark:text-slate-500">
                <span class="flex items-center space-x-0.5">
                  <Eye class="w-3 h-3 text-slate-400" />
                  <span>{{ post.viewCount }}</span>
                </span>
                <span>·</span>
                <span>{{ formatDate(post.createdAt).slice(0, 10) }}</span>
              </div>
            </div>

            <!-- 卡片主体：封面 + 标题 + Slug (彻底告别横向溢出) -->
            <div class="flex items-start space-x-3">
              <div class="w-14 h-14 rounded-xl bg-pink-100 dark:bg-space-900 border border-pink-200/50 dark:border-white/10 overflow-hidden shrink-0 shadow-sm">
                <img
                  v-if="post.coverImage"
                  :src="post.coverImage"
                  :alt="post.title"
                  class="w-full h-full object-cover"
                  onerror="this.src='/images/hsr/himeko_express.webp'"
                />
                <div v-else class="w-full h-full flex items-center justify-center text-slate-400 dark:text-slate-600">
                  <BookOpen class="w-5 h-5" />
                </div>
              </div>

              <div class="flex-1 min-w-0">
                <router-link
                  :to="`/admin/posts/edit/${post.id}`"
                  class="font-semibold text-slate-800 dark:text-slate-100 hover:text-pink-600 dark:hover:text-amber-400 transition-colors line-clamp-2 text-sm leading-snug break-words"
                >
                  {{ post.title }}
                </router-link>
                <p class="text-[11px] text-slate-400 dark:text-slate-500 font-mono truncate mt-1">
                  /posts/{{ post.slug }}
                </p>
              </div>
            </div>

            <!-- 卡片底部：标签分类 + 快捷操作按键 -->
            <div class="flex items-center justify-between pt-1 gap-2">
              <!-- 标签 -->
              <div class="flex flex-wrap gap-1 items-center min-w-0 flex-1">
                <span
                  v-for="tag in post.tags"
                  :key="tag.id"
                  class="px-1.5 py-0.5 rounded text-[10px] font-mono border truncate max-w-[100px]"
                  :style="{
                    backgroundColor: `${tag.color || '#6366f1'}15`,
                    color: tag.color || '#6366f1',
                    borderColor: `${tag.color || '#6366f1'}30`
                  }"
                >
                  {{ tag.name }}
                </span>
                <span v-if="!post.tags || post.tags.length === 0" class="text-slate-400 dark:text-slate-600 text-[10px]">
                  无标签
                </span>
              </div>

              <!-- 操作按键 -->
              <div class="flex items-center space-x-1.5 shrink-0">
                <router-link
                  :to="`/admin/posts/edit/${post.id}`"
                  class="px-2.5 py-1 rounded-lg text-xs font-medium bg-amber-500/10 text-amber-700 dark:text-amber-300 border border-amber-400/30 flex items-center space-x-1 hover:bg-amber-500/20 transition-colors"
                >
                  <Edit3 class="w-3.5 h-3.5" />
                  <span>编辑</span>
                </router-link>

                <a
                  :href="`/posts/${post.id}`"
                  target="_blank"
                  class="p-1.5 rounded-lg text-slate-500 dark:text-slate-400 hover:text-pink-600 dark:hover:text-nebula-cyan hover:bg-pink-100/60 dark:hover:bg-white/5 border border-transparent hover:border-pink-200/50 transition-colors"
                  title="预览"
                >
                  <ExternalLink class="w-3.5 h-3.5" />
                </a>

                <button
                  @click="handleDelete(post)"
                  class="p-1.5 rounded-lg text-rose-500 hover:text-rose-600 hover:bg-rose-500/10 border border-transparent hover:border-rose-400/30 transition-colors"
                  title="删除"
                >
                  <Trash2 class="w-3.5 h-3.5" />
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 2. 桌面端/平板端：高密度大屏表格 (Desktop Table View, hidden md:block) -->
        <div class="hidden md:block overflow-x-auto">
          <table class="w-full text-left border-collapse">
            <thead>
              <tr class="border-b border-pink-200/40 dark:border-white/10 bg-pink-500/5 dark:bg-white/5 text-[11px] font-mono uppercase tracking-wider text-slate-600 dark:text-slate-400">
                <th class="py-3 px-4 w-12">#</th>
                <th class="py-3 px-4 min-w-[200px]">文章标题 / Slug</th>
                <th class="py-3 px-4 w-32">标签分类</th>
                <th class="py-3 px-4 w-28 text-center">发布状态</th>
                <th class="py-3 px-4 w-20 text-center">置顶</th>
                <th class="py-3 px-4 w-24 text-right">阅读量</th>
                <th class="py-3 px-4 w-36">创建时间</th>
                <th class="py-3 px-4 w-32 text-center">操作</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-pink-200/30 dark:divide-white/5 text-xs text-slate-700 dark:text-slate-300">
              <tr
                v-for="post in filteredPosts"
                :key="'desktop-' + post.id"
                class="hover:bg-pink-500/[0.04] dark:hover:bg-white/[0.03] transition-colors group"
              >
                <!-- ID -->
                <td class="py-3 px-4 font-mono text-slate-400 dark:text-slate-500">
                  {{ post.id }}
                </td>

                <!-- 标题与封面 -->
                <td class="py-3 px-4">
                  <div class="flex items-center space-x-3 min-w-0">
                    <div class="w-10 h-10 rounded-lg bg-pink-100 dark:bg-space-900 border border-pink-200/50 dark:border-white/10 overflow-hidden shrink-0">
                      <img
                        v-if="post.coverImage"
                        :src="post.coverImage"
                        :alt="post.title"
                        class="w-full h-full object-cover"
                        onerror="this.src='/images/hsr/himeko_express.webp'"
                      />
                      <div v-else class="w-full h-full flex items-center justify-center text-slate-400 dark:text-slate-600">
                        <BookOpen class="w-4 h-4" />
                      </div>
                    </div>
                    <div class="min-w-0 flex-1 max-w-sm lg:max-w-md">
                      <router-link
                        :to="`/admin/posts/edit/${post.id}`"
                        class="font-semibold text-slate-800 dark:text-slate-100 hover:text-pink-600 dark:hover:text-amber-400 transition-colors line-clamp-1 block break-words"
                      >
                        {{ post.title }}
                      </router-link>
                      <p class="text-[11px] text-slate-500 dark:text-slate-400 font-mono truncate mt-0.5" :title="'/posts/' + post.slug">
                        /posts/{{ post.slug }}
                      </p>
                    </div>
                  </div>
                </td>

                <!-- 标签 -->
                <td class="py-3 px-4">
                  <div class="flex flex-wrap gap-1">
                    <span
                      v-for="tag in post.tags"
                      :key="tag.id"
                      class="px-2 py-0.5 rounded-md text-[10px] font-mono border"
                      :style="{
                        backgroundColor: `${tag.color || '#6366f1'}15`,
                        color: tag.color || '#6366f1',
                        borderColor: `${tag.color || '#6366f1'}30`
                      }"
                    >
                      {{ tag.name }}
                    </span>
                    <span v-if="!post.tags || post.tags.length === 0" class="text-slate-400 dark:text-slate-600 text-[10px]">
                      无
                    </span>
                  </div>
                </td>

                <!-- 状态快捷切换 -->
                <td class="py-3 px-4 text-center">
                  <button
                    @click="toggleStatus(post)"
                    class="px-2.5 py-1 rounded-full text-[11px] font-medium transition-all"
                    :class="isPublished(post) ? 'bg-emerald-500/15 text-emerald-700 dark:text-emerald-300 border border-emerald-500/30 hover:bg-emerald-500/25' : 'bg-amber-500/15 text-amber-700 dark:text-amber-300 border border-amber-500/30 hover:bg-amber-500/25'"
                    :title="isPublished(post) ? '点击转为私密草稿' : '点击公开发布文章'"
                  >
                    {{ isPublished(post) ? '● 已发布' : '○ 草稿箱' }}
                  </button>
                </td>

                <!-- 置顶快捷切换 -->
                <td class="py-3 px-4 text-center">
                  <button
                    @click="togglePin(post)"
                    class="p-1.5 rounded-lg transition-colors"
                    :class="isPinned(post) ? 'text-amber-500 dark:text-amber-400 hover:text-amber-600 dark:hover:text-amber-300' : 'text-slate-400 dark:text-slate-600 hover:text-slate-600 dark:hover:text-slate-400'"
                    :title="isPinned(post) ? '取消置顶' : '置顶文章'"
                  >
                    <Pin class="w-4 h-4" :class="{ 'fill-amber-400': isPinned(post) }" />
                  </button>
                </td>

                <!-- 阅读量 -->
                <td class="py-3 px-4 text-right font-mono text-slate-600 dark:text-slate-400">
                  {{ post.viewCount }}
                </td>

                <!-- 创建时间 -->
                <td class="py-3 px-4 text-[11px] font-mono text-slate-500">
                  {{ formatDate(post.createdAt) }}
                </td>

                <!-- 操作区 -->
                <td class="py-3 px-4 text-center">
                  <div class="flex items-center justify-center space-x-1.5">
                    <!-- 编辑 -->
                    <router-link
                      :to="`/admin/posts/edit/${post.id}`"
                      class="p-1.5 rounded-lg text-slate-500 dark:text-slate-400 hover:text-amber-500 dark:hover:text-amber-400 hover:bg-pink-100/60 dark:hover:bg-white/5 transition-colors"
                      title="编辑文章"
                    >
                      <Edit3 class="w-4 h-4" />
                    </router-link>

                    <!-- 前台预览 -->
                    <a
                      :href="`/posts/${post.id}`"
                      target="_blank"
                      class="p-1.5 rounded-lg text-slate-500 dark:text-slate-400 hover:text-pink-600 dark:hover:text-nebula-cyan hover:bg-pink-100/60 dark:hover:bg-white/5 transition-colors"
                      title="新窗口预览"
                    >
                      <ExternalLink class="w-4 h-4" />
                    </a>

                    <!-- 删除 -->
                    <button
                      @click="handleDelete(post)"
                      class="p-1.5 rounded-lg text-slate-500 dark:text-slate-400 hover:text-rose-600 dark:hover:text-rose-400 hover:bg-rose-500/10 transition-colors"
                      title="删除文章"
                    >
                      <Trash2 class="w-4 h-4" />
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import {
  BookOpen,
  CheckCircle,
  FileClock,
  Eye,
  Search,
  RefreshCw,
  Plus,
  Loader2,
  Pin,
  Edit3,
  ExternalLink,
  Trash2
} from 'lucide-vue-next';
import { getAdminPosts, deletePost, updatePostStatus, updatePostPin } from '@/api/admin';
import type { PostListVO } from '@/types';
import { useToast } from '@/composables/useToast';

const toast = useToast();
const posts = ref<PostListVO[]>([]);
const loading = ref(false);
const searchQuery = ref('');
const filterStatus = ref<'ALL' | 'PUBLISHED' | 'DRAFT'>('ALL');

const isPinned = (post: PostListVO): boolean => {
  return post.isPinned === true || post.isPinned === 1;
};

const isPublished = (post: PostListVO): boolean => {
  return (post.status || '').toUpperCase() === 'PUBLISHED';
};

const publishedCount = computed(() => {
  return posts.value.filter(p => (p.status || '').toUpperCase() === 'PUBLISHED').length;
});

const draftCount = computed(() => {
  return posts.value.filter(p => (p.status || '').toUpperCase() === 'DRAFT').length;
});

const totalViews = computed(() => {
  return posts.value.reduce((acc, cur) => acc + (cur.viewCount || 0), 0);
});

const filteredPosts = computed(() => {
  return posts.value.filter(post => {
    // 状态过滤
    if (filterStatus.value !== 'ALL') {
      const currentStatus = (post.status || '').toUpperCase();
      if (currentStatus !== filterStatus.value) {
        return false;
      }
    }
    // 搜索词匹配
    if (searchQuery.value.trim()) {
      const q = searchQuery.value.toLowerCase();
      const matchTitle = post.title?.toLowerCase().includes(q);
      const matchSlug = post.slug?.toLowerCase().includes(q);
      return matchTitle || matchSlug;
    }
    return true;
  });
});

const fetchPosts = async () => {
  loading.value = true;
  try {
    const list = await getAdminPosts();
    posts.value = list || [];
  } catch (err: any) {
    console.error('获取文章列表失败:', err);
    toast.error(err.response?.data?.message || err.message || '获取文章列表失败');
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateStr?: string) => {
  if (!dateStr) return '-';
  return dateStr.replace('T', ' ').slice(0, 16);
};

// 快捷切换状态 (轻量原子更新)
const toggleStatus = async (post: PostListVO) => {
  const newStatus: 'PUBLISHED' | 'DRAFT' = isPublished(post) ? 'DRAFT' : 'PUBLISHED';
  try {
    await updatePostStatus(post.id, newStatus);
    post.status = newStatus;
    toast.success(newStatus === 'PUBLISHED' ? '文章已设为公开上线 🌟' : '文章已转为私密草稿 📝');
  } catch (err: any) {
    toast.error(err.response?.data?.message || err.message || '切换状态失败');
  }
};

// 快捷切换置顶 (轻量原子更新)
const togglePin = async (post: PostListVO) => {
  const newPinned = !isPinned(post);
  try {
    await updatePostPin(post.id, newPinned ? 1 : 0);
    post.isPinned = newPinned ? 1 : 0;
    toast.success(newPinned ? '文章已置顶 📌' : '已取消置顶');
  } catch (err: any) {
    toast.error(err.response?.data?.message || err.message || '切换置顶失败');
  }
};

// 删除文章
const handleDelete = async (post: PostListVO) => {
  if (confirm(`⚠️ 危险操作：确定要彻底删除文章《${post.title}》吗？此操作不可逆！`)) {
    try {
      await deletePost(post.id);
      posts.value = posts.value.filter(p => p.id !== post.id);
      toast.success('文章已彻底删除 🗑️');
    } catch (err: any) {
      toast.error(err.response?.data?.message || err.message || '删除文章失败');
    }
  }
};

onMounted(() => {
  fetchPosts();
});
</script>
