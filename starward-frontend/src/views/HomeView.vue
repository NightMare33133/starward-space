<template>
  <div class="space-y-20 pt-8 pb-16">
    <!-- 英雄区 (Hero Section) -->
    <section class="text-center relative py-12 px-4 max-w-4xl mx-auto space-y-8">
      <!-- 头像光环 -->
      <div class="relative inline-block group">
        <div class="w-28 h-28 sm:w-32 sm:h-32 rounded-full p-[2px] bg-gradient-to-tr from-nebula-cyan via-nebula-purple to-nebula-pink animate-pulse-slow shadow-2xl shadow-nebula-cyan/30">
          <img
            src="https://github.com/NightMare33133.png"
            alt="NightMare33133"
            class="w-full h-full object-cover rounded-full group-hover:scale-105 transition-transform duration-500"
          />
        </div>
        <!-- 在线状态标识 -->
        <div class="absolute bottom-1 right-2 w-5 h-5 rounded-full bg-space-950 flex items-center justify-center">
          <div class="w-3.5 h-3.5 rounded-full bg-emerald-400 animate-pulse"></div>
        </div>
      </div>

      <!-- 主标题与副标 -->
      <div class="space-y-4">
        <h1 class="text-3xl sm:text-5xl font-extrabold tracking-tight text-white">
          向星光深处航行，<br class="sm:hidden" />
          <span class="gradient-text-star">记录每一次思考与探索</span>
        </h1>
        <p class="text-slate-300 text-sm sm:text-base max-w-2xl mx-auto leading-relaxed">
          你好，我是 <span class="text-white font-semibold">NightMare33133</span>。这里是我的个人全栈知识空间与数字分身试验田。
          <br class="hidden sm:inline" />
          福州大学 CS ➔ 香港城市大学 & 复旦大学联合培养 · 专注全栈架构、AI 智能体与极客创造。
        </p>
      </div>

      <!-- 快捷入口按钮组 -->
      <div class="flex flex-wrap items-center justify-center gap-4 pt-2">
        <router-link
          to="/posts"
          class="px-6 py-2.5 rounded-full bg-gradient-to-r from-nebula-cyan to-nebula-purple text-white font-semibold text-sm shadow-lg shadow-nebula-cyan/25 hover:shadow-nebula-cyan/40 hover:scale-105 active:scale-95 transition-all flex items-center space-x-2"
        >
          <BookOpen class="w-4 h-4" />
          <span>浏览深度文章</span>
        </router-link>
        <router-link
          to="/moments"
          class="px-6 py-2.5 rounded-full glass-card hover:bg-white/10 text-slate-200 font-medium text-sm border border-white/10 hover:border-white/20 transition-all flex items-center space-x-2"
        >
          <Sparkles class="w-4 h-4 text-amber-400" />
          <span>星际碎语动态</span>
        </router-link>
        <router-link
          to="/gallery"
          class="px-6 py-2.5 rounded-full glass-card hover:bg-white/10 text-slate-200 font-medium text-sm border border-white/10 hover:border-white/20 transition-all flex items-center space-x-2"
        >
          <Camera class="w-4 h-4 text-nebula-pink" />
          <span>摄影视界</span>
        </router-link>
      </div>

      <!-- 核心指标卡片 -->
      <div class="grid grid-cols-3 gap-4 pt-6 max-w-lg mx-auto">
        <div class="glass-card p-4 rounded-2xl text-center">
          <div class="text-2xl font-bold font-mono text-white">{{ posts.length }}</div>
          <div class="text-[11px] text-slate-400 mt-1">深度文章</div>
        </div>
        <div class="glass-card p-4 rounded-2xl text-center">
          <div class="text-2xl font-bold font-mono text-nebula-cyan">{{ moments.length }}</div>
          <div class="text-[11px] text-slate-400 mt-1">星际碎语</div>
        </div>
        <div class="glass-card p-4 rounded-2xl text-center">
          <div class="text-2xl font-bold font-mono text-nebula-purple">100%</div>
          <div class="text-[11px] text-slate-400 mt-1">自主可控</div>
        </div>
      </div>
    </section>

    <!-- 精选文章模块 (Recent Posts) -->
    <section class="max-w-5xl mx-auto px-4 sm:px-6 space-y-6">
      <div class="flex items-center justify-between border-b border-white/10 pb-4">
        <div class="flex items-center space-x-3">
          <div class="w-2.5 h-6 rounded-full bg-nebula-cyan"></div>
          <h2 class="text-xl font-bold text-white tracking-wide">精选文章 · Recent Posts</h2>
        </div>
        <router-link to="/posts" class="text-xs text-nebula-cyan hover:underline flex items-center space-x-1">
          <span>查看全部</span>
          <ChevronRight class="w-3.5 h-3.5" />
        </router-link>
      </div>

      <!-- 加载状态 -->
      <div v-if="loadingPosts" class="text-center py-12 text-slate-400 text-sm">
        <div class="inline-block w-6 h-6 border-2 border-nebula-cyan border-t-transparent rounded-full animate-spin mb-2"></div>
        <p>正在从本地星际数据库加载文章...</p>
      </div>

      <!-- 文章网格 -->
      <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
        <article
          v-for="post in posts"
          :key="post.id"
          class="glass-card rounded-2xl p-6 flex flex-col justify-between group cursor-pointer"
          @click="$router.push(`/posts/${post.id}`)"
        >
          <div class="space-y-3">
            <!-- 标签与置顶 -->
            <div class="flex items-center gap-2 flex-wrap">
              <span v-if="post.isPinned" class="px-2 py-0.5 rounded-full text-[10px] font-bold bg-amber-500/20 text-amber-300 border border-amber-500/30">
                置顶
              </span>
              <span
                v-for="tag in post.tags"
                :key="tag.id"
                class="px-2.5 py-0.5 rounded-full text-[10px] font-medium bg-nebula-cyan/10 text-nebula-cyan border border-nebula-cyan/20"
              >
                # {{ tag.name }}
              </span>
            </div>

            <!-- 标题 -->
            <h3 class="text-lg font-bold text-slate-100 group-hover:text-nebula-cyan transition-colors line-clamp-2">
              {{ post.title }}
            </h3>

            <!-- 摘要 -->
            <p class="text-xs text-slate-400 leading-relaxed line-clamp-3">
              {{ post.summary }}
            </p>
          </div>

          <!-- 底栏：阅读量与日期 -->
          <div class="mt-6 pt-4 border-t border-white/5 flex items-center justify-between text-xs text-slate-500 font-mono">
            <span class="flex items-center space-x-1">
              <Calendar class="w-3.5 h-3.5" />
              <span>{{ formatDate(post.publishedAt) }}</span>
            </span>
            <span class="flex items-center space-x-1">
              <Eye class="w-3.5 h-3.5" />
              <span>{{ post.viewCount }} 阅读</span>
            </span>
          </div>
        </article>
      </div>
    </section>

    <!-- 最新星际碎语预览 (Moments Preview) -->
    <section class="max-w-5xl mx-auto px-4 sm:px-6 space-y-6">
      <div class="flex items-center justify-between border-b border-white/10 pb-4">
        <div class="flex items-center space-x-3">
          <div class="w-2.5 h-6 rounded-full bg-nebula-purple"></div>
          <h2 class="text-xl font-bold text-white tracking-wide">最新星际碎语 · Moments</h2>
        </div>
        <router-link to="/moments" class="text-xs text-nebula-purple hover:underline flex items-center space-x-1">
          <span>进入时间线</span>
          <ChevronRight class="w-3.5 h-3.5" />
        </router-link>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div
          v-for="moment in moments.slice(0, 4)"
          :key="moment.id"
          class="glass-card rounded-2xl p-5 space-y-3"
        >
          <div class="flex items-center justify-between text-xs text-slate-500 font-mono">
            <div class="flex items-center space-x-1.5 text-slate-400">
              <span class="text-base">{{ moment.moodEmoji || '✨' }}</span>
              <span v-if="moment.location" class="flex items-center space-x-1 text-slate-400">
                <MapPin class="w-3 h-3 text-nebula-pink" />
                <span>{{ moment.location }}</span>
              </span>
            </div>
            <span>{{ formatDate(moment.createdAt) }}</span>
          </div>
          <p class="text-sm text-slate-200 leading-relaxed">
            {{ moment.content }}
          </p>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { BookOpen, Sparkles, Camera, ChevronRight, Calendar, Eye, MapPin } from 'lucide-vue-next';
import { getPostList } from '@/api/posts';
import { getMoments } from '@/api/moments';
import type { PostListVO, Moment } from '@/types';

const posts = ref<PostListVO[]>([]);
const moments = ref<Moment[]>([]);
const loadingPosts = ref(true);

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`;
};

onMounted(async () => {
  try {
    const [postsData, momentsData] = await Promise.all([
      getPostList().catch(() => []),
      getMoments().catch(() => [])
    ]);
    posts.value = postsData;
    moments.value = momentsData;
  } finally {
    loadingPosts.value = false;
  }
});
</script>
