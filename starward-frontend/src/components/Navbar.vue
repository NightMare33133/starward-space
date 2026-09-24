<template>
  <header class="fixed top-0 left-0 right-0 z-40 transition-all duration-300" :class="{ 'py-3 backdrop-blur-xl bg-space-950/70 border-b border-white/10 shadow-lg': isScrolled, 'py-5 bg-transparent': !isScrolled }">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 flex items-center justify-between">
      <!-- 品牌标识 Logo -->
      <router-link to="/" class="flex items-center space-x-3 group">
        <div class="w-9 h-9 rounded-xl bg-gradient-to-tr from-nebula-cyan to-nebula-purple p-[1px] shadow-lg shadow-nebula-cyan/20 group-hover:scale-105 transition-transform">
          <div class="w-full h-full bg-space-950 rounded-[11px] flex items-center justify-center">
            <span class="text-base font-bold bg-gradient-to-r from-nebula-cyan to-nebula-pink bg-clip-text text-transparent">✦</span>
          </div>
        </div>
        <div class="flex flex-col">
          <div class="flex items-center space-x-1.5">
            <span class="text-base font-bold tracking-wider text-slate-100 group-hover:text-nebula-cyan transition-colors">STARWARD</span>
            <span class="text-xs text-slate-400 font-medium">の 星向空间</span>
          </div>
          <span class="text-[9px] tracking-wider text-nebula-cyan/80 font-mono -mt-0.5">愿此行，终抵群星 ✦</span>
        </div>
      </router-link>

      <!-- 桌面端导航链接 -->
      <nav class="hidden md:flex items-center space-x-1 glass-card px-4 py-1.5 rounded-full border border-white/10">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="px-4 py-1.5 rounded-full text-sm font-medium transition-all duration-200"
          :class="$route.path === item.path ? 'text-white bg-white/10 shadow-sm' : 'text-slate-400 hover:text-slate-100 hover:bg-white/5'"
        >
          <div class="flex items-center space-x-1.5">
            <component :is="item.icon" class="w-4 h-4" />
            <span>{{ item.title }}</span>
          </div>
        </router-link>
      </nav>

      <!-- 右侧操作区：音乐岛触发器、主题切换、GitHub 仓库 -->
      <div class="flex items-center space-x-2.5">
        <!-- 音乐播放状态指示小图标 -->
        <button
          @click="musicStore.isExpanded = !musicStore.isExpanded"
          class="p-2 rounded-xl text-slate-400 hover:text-nebula-cyan hover:bg-white/5 transition-all relative"
          title="星际音乐岛"
        >
          <Music class="w-5 h-5" :class="{ 'text-nebula-cyan animate-pulse': musicStore.isPlaying }" />
          <span v-if="musicStore.isPlaying" class="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-nebula-cyan animate-ping"></span>
        </button>

        <!-- GitHub 外部直链 -->
        <a
          href="https://github.com/NightMare33133/starward-space"
          target="_blank"
          rel="noopener noreferrer"
          class="p-2 rounded-xl text-slate-400 hover:text-white hover:bg-white/5 transition-all"
          title="GitHub 仓库"
        >
          <Github class="w-5 h-5" />
        </a>

        <!-- 移动端汉堡菜单按钮 -->
        <button
          @click="mobileMenuOpen = !mobileMenuOpen"
          class="md:hidden p-2 rounded-xl text-slate-400 hover:text-white hover:bg-white/5"
        >
          <Menu v-if="!mobileMenuOpen" class="w-5 h-5" />
          <X v-else class="w-5 h-5" />
        </button>
      </div>
    </div>

    <!-- 移动端抽屉菜单 -->
    <div
      v-if="mobileMenuOpen"
      class="md:hidden fixed inset-x-0 top-[65px] bg-space-950/95 backdrop-blur-2xl border-b border-white/10 p-5 space-y-3 z-50"
    >
      <router-link
        v-for="item in navItems"
        :key="item.path"
        :to="item.path"
        @click="mobileMenuOpen = false"
        class="flex items-center space-x-3 px-4 py-3 rounded-xl text-slate-300 hover:text-white hover:bg-white/5 transition-all"
        :class="{ 'bg-white/10 text-nebula-cyan font-bold': $route.path === item.path }"
      >
        <component :is="item.icon" class="w-5 h-5 text-nebula-cyan" />
        <span>{{ item.title }}</span>
      </router-link>
    </div>
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { Home, BookOpen, MessageSquare, Camera, User, Github, Music, Menu, X } from 'lucide-vue-next';
import { useMusicStore } from '@/stores/music';

const musicStore = useMusicStore();
const isScrolled = ref(false);
const mobileMenuOpen = ref(false);

const navItems = [
  { title: '首页', path: '/', icon: Home },
  { title: '文章', path: '/posts', icon: BookOpen },
  { title: '星际碎语', path: '/moments', icon: MessageSquare },
  { title: '摄影视界', path: '/gallery', icon: Camera },
  { title: '关于我', path: '/about', icon: User },
];

const checkScroll = () => {
  isScrolled.value = window.scrollY > 20;
};

onMounted(() => {
  window.addEventListener('scroll', checkScroll);
  checkScroll();
});

onUnmounted(() => {
  window.removeEventListener('scroll', checkScroll);
});
</script>
