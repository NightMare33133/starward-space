<template>
  <div class="min-h-screen flex flex-col">
    <!-- 列车长控制中枢顶部导航栏 -->
    <header class="sticky top-0 z-30 bg-white/80 dark:bg-space-950/80 backdrop-blur-xl border-b border-pink-200/50 dark:border-white/10 shadow-lg transition-colors duration-500">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between">
        <!-- 左侧列车长标识 -->
        <div class="flex items-center space-x-3">
          <div class="w-9 h-9 rounded-xl bg-gradient-to-tr from-amber-400 via-nebula-pink to-nebula-cyan p-[1.5px] shadow-lg shadow-amber-500/20 shrink-0">
            <div class="w-full h-full rounded-[10px] overflow-hidden bg-pink-100 dark:bg-space-950">
              <img
                src="/images/pompom.webp"
                alt="Pom-Pom"
                class="w-full h-full object-cover"
              />
            </div>
          </div>
          <div>
            <div class="flex items-center space-x-2">
              <span class="text-sm font-bold tracking-wider text-slate-800 dark:text-slate-100">星轨控制中枢</span>
              <span class="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-amber-400/20 text-amber-700 dark:text-amber-300 border border-amber-400/40">
                列车长在线
              </span>
            </div>
            <p class="text-[10px] text-slate-500 dark:text-slate-400 font-mono">STARWARD CMS · Command Deck</p>
          </div>
        </div>

        <!-- 中间功能标签页导航 -->
        <nav class="hidden md:flex items-center space-x-1 glass-card px-3 py-1.5 rounded-2xl border border-pink-200/50 dark:border-white/10">
          <router-link
            v-for="item in navTabs"
            :key="item.path"
            :to="item.path"
            class="px-3.5 py-1.5 rounded-xl text-xs font-medium transition-all duration-200 flex items-center space-x-1.5"
            :class="isActiveTab(item.path) ? 'text-amber-700 dark:text-white bg-amber-500/15 dark:bg-gradient-to-r dark:from-amber-500/30 dark:to-nebula-cyan/30 border border-amber-400/40 shadow-sm font-bold' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-100 hover:bg-black/5 dark:hover:bg-white/5'"
          >
            <component :is="item.icon" class="w-3.5 h-3.5 text-amber-500 dark:text-amber-400" />
            <span>{{ item.title }}</span>
          </router-link>
        </nav>

        <!-- 右侧主题切换、音乐控制、返回前台与注销 -->
        <div class="flex items-center space-x-2">
          <!-- 日夜双模切换胶囊 (落樱绯英 vs 深空流萤) -->
          <button
            @click="themeStore.toggleTheme($event)"
            class="p-2 w-8 h-8 rounded-xl border border-pink-200/60 dark:border-white/10 hover:border-amber-400/50 bg-white/60 dark:bg-white/5 text-slate-600 dark:text-slate-300 hover:text-amber-500 transition-all flex items-center justify-center shadow-sm overflow-hidden"
            :title="themeStore.isDark ? '切换至日间模式 (绯英 · 落樱)' : '切换至夜间模式 (流萤 · 深空)'"
          >
            <transition name="icon-spin" mode="out-in">
              <Sun v-if="themeStore.isDark" key="sun" class="w-4 h-4 text-amber-300 rotate-0 transition-transform duration-500" />
              <Moon v-else key="moon" class="w-4 h-4 text-indigo-600 -rotate-12 transition-transform duration-500" />
            </transition>
          </button>

          <!-- 音乐岛触发器 -->
          <button
            @click="musicStore.isExpanded = !musicStore.isExpanded"
            class="p-2 rounded-xl text-slate-600 dark:text-slate-400 hover:text-pink-600 dark:hover:text-nebula-cyan hover:bg-pink-100/50 dark:hover:bg-white/5 transition-all relative"
            title="星际音乐岛"
          >
            <Music class="w-4 h-4" :class="{ 'text-pink-600 dark:text-nebula-cyan animate-pulse': musicStore.isPlaying }" />
            <span v-if="musicStore.isPlaying" class="absolute top-1 right-1 w-2 h-2 rounded-full bg-pink-500 dark:bg-nebula-cyan animate-ping"></span>
          </button>

          <router-link
            to="/"
            class="px-3 py-1.5 rounded-xl border border-pink-200/60 dark:border-white/10 hover:border-pink-300 dark:hover:border-nebula-cyan/40 bg-white/60 dark:bg-white/5 hover:bg-pink-50 dark:hover:bg-white/10 text-xs text-slate-700 dark:text-slate-300 hover:text-slate-900 dark:hover:text-white transition-all flex items-center space-x-1.5 shadow-sm"
            title="返回星向空间前台"
          >
            <ExternalLink class="w-3.5 h-3.5 text-pink-600 dark:text-nebula-cyan" />
            <span class="hidden sm:inline">浏览前台</span>
          </router-link>

          <button
            @click="handleLogout"
            class="px-3 py-1.5 rounded-xl border border-rose-500/20 hover:border-rose-500/40 bg-rose-500/10 hover:bg-rose-500/20 text-xs text-rose-600 dark:text-rose-300 transition-all flex items-center space-x-1.5 shadow-sm"
            title="退出列车长中枢"
          >
            <LogOut class="w-3.5 h-3.5" />
            <span class="hidden sm:inline">注销密令</span>
          </button>
        </div>
      </div>

      <!-- 移动端二级导航条 -->
      <div class="md:hidden flex items-center justify-around border-t border-pink-200/30 dark:border-white/5 py-2 px-2 bg-white/70 dark:bg-space-950/60 overflow-x-auto">
        <router-link
          v-for="item in navTabs"
          :key="item.path"
          :to="item.path"
          class="px-3 py-1 rounded-lg text-xs font-medium shrink-0 flex items-center space-x-1"
          :class="isActiveTab(item.path) ? 'text-amber-700 dark:text-amber-300 bg-amber-500/20 font-bold' : 'text-slate-600 dark:text-slate-400'"
        >
          <component :is="item.icon" class="w-3 h-3" />
          <span>{{ item.title }}</span>
        </router-link>
      </div>
    </header>

    <!-- 子页面内容视图 -->
    <main class="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 py-6">
      <router-view v-slot="{ Component }">
        <transition
          enter-active-class="transition duration-200 ease-out"
          enter-from-class="opacity-0 translate-y-2"
          enter-to-class="opacity-100 translate-y-0"
          leave-active-class="transition duration-150 ease-in"
          leave-from-class="opacity-100 translate-y-0"
          leave-to-class="opacity-0 -translate-y-2"
          mode="out-in"
        >
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import {
  FileText,
  PenTool,
  Tag as TagIcon,
  MessageSquare,
  ExternalLink,
  LogOut,
  Music,
  Sun,
  Moon
} from 'lucide-vue-next';
import { useMusicStore } from '@/stores/music';
import { useThemeStore } from '@/stores/theme';

const route = useRoute();
const router = useRouter();
const musicStore = useMusicStore();
const themeStore = useThemeStore();

const navTabs = [
  { title: '文章管理', path: '/admin/posts', icon: FileText },
  { title: '新建文章', path: '/admin/posts/new', icon: PenTool },
  { title: '标签管理', path: '/admin/tags', icon: TagIcon },
  { title: '星际碎语', path: '/admin/moments', icon: MessageSquare },
];

const isActiveTab = (path: string) => {
  if (path === '/admin/posts') {
    return route.path === '/admin/posts' || route.path.startsWith('/admin/posts/edit');
  }
  return route.path === path;
};

const handleLogout = () => {
  if (confirm('确认注销当前列车长密令凭证？')) {
    localStorage.removeItem('starward_admin_token');
    router.push('/');
  }
};
</script>
