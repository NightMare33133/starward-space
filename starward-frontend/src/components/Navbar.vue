<template>
  <header class="fixed top-0 left-0 right-0 z-40 transition-all duration-300" :class="{ 'py-3 backdrop-blur-xl bg-white/75 dark:bg-space-950/70 border-b border-pink-200/40 dark:border-white/10 shadow-lg shadow-pink-500/5 dark:shadow-none': isScrolled, 'py-5 bg-transparent': !isScrolled }">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 flex items-center justify-between">
      <!-- 品牌标识 Logo (帕姆列车长) - 支持三击触发星轨密令彩蛋 -->
      <router-link to="/" class="flex items-center space-x-3 group" @click="handleLogoClick">
        <div class="w-10 h-10 rounded-2xl bg-gradient-to-tr from-amber-400 via-nebula-pink to-nebula-cyan p-[1.5px] shadow-lg shadow-amber-500/25 group-hover:scale-110 active:scale-95 transition-transform overflow-hidden shrink-0 cursor-pointer">
          <div class="w-full h-full rounded-[14px] overflow-hidden bg-space-950">
            <img
              src="/images/pompom.png"
              alt="Pom-Pom"
              class="w-full h-full object-cover group-hover:rotate-6 transition-transform duration-300"
            />
          </div>
        </div>
        <div class="flex flex-col">
          <div class="flex items-center space-x-1.5">
            <span class="text-base font-bold tracking-wider text-slate-800 dark:text-slate-100 group-hover:text-pink-600 dark:group-hover:text-nebula-cyan transition-colors">STARWARD</span>
            <span class="text-xs text-slate-500 dark:text-slate-400 font-medium">starlight の 星向空间</span>
          </div>
          <span class="text-[9px] tracking-wider text-pink-500/90 dark:text-nebula-cyan/80 font-mono -mt-0.5">愿此行，终抵群星 ✦</span>
        </div>
      </router-link>

      <!-- 桌面端导航链接 -->
      <nav class="hidden md:flex items-center space-x-1 glass-card px-4 py-1.5 rounded-full border border-pink-200/40 dark:border-white/10">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="px-4 py-1.5 rounded-full text-sm font-medium transition-all duration-200"
          :class="$route.path === item.path ? 'text-pink-600 bg-pink-100/70 dark:text-white dark:bg-white/10 shadow-sm font-semibold' : 'text-slate-600 hover:text-pink-600 hover:bg-pink-50/60 dark:text-slate-400 dark:hover:text-slate-100 dark:hover:bg-white/5'"
        >
          <div class="flex items-center space-x-1.5">
            <component :is="item.icon" class="w-4 h-4" />
            <span>{{ item.title }}</span>
          </div>
        </router-link>
      </nav>

      <!-- 右侧操作区：主题切换、音乐岛触发器、GitHub 仓库 -->
      <div class="flex items-center space-x-2">
        <!-- 日夜沉浸式主题切换按钮 -->
        <button
          @click="themeStore.toggleTheme"
          class="p-2 rounded-xl transition-all relative group overflow-hidden"
          :class="themeStore.isDark ? 'text-amber-300 hover:text-amber-200 hover:bg-white/10' : 'text-pink-600 hover:text-pink-700 hover:bg-pink-100/60'"
          :title="themeStore.isDark ? '切换至日间模式 · 落樱晨曦' : '切换至夜间模式 · 深空流萤'"
        >
          <Sun v-if="themeStore.isDark" class="w-5 h-5 transition-transform duration-500 group-hover:rotate-45" />
          <Moon v-else class="w-5 h-5 transition-transform duration-500 group-hover:-rotate-12" />
        </button>

        <!-- 音乐播放状态指示小图标 -->
        <button
          @click="musicStore.isExpanded = !musicStore.isExpanded"
          class="p-2 rounded-xl text-slate-500 hover:text-pink-600 hover:bg-pink-100/50 dark:text-slate-400 dark:hover:text-nebula-cyan dark:hover:bg-white/5 transition-all relative"
          title="星际音乐岛"
        >
          <Music class="w-5 h-5" :class="{ 'text-pink-600 dark:text-nebula-cyan animate-pulse': musicStore.isPlaying }" />
          <span v-if="musicStore.isPlaying" class="absolute top-1.5 right-1.5 w-2 h-2 rounded-full bg-pink-500 dark:bg-nebula-cyan animate-ping"></span>
        </button>

        <!-- GitHub 外部直链 -->
        <a
          href="https://github.com/NightMare33133/starward-space"
          target="_blank"
          rel="noopener noreferrer"
          class="p-2 rounded-xl text-slate-500 hover:text-slate-900 hover:bg-pink-100/50 dark:text-slate-400 dark:hover:text-white dark:hover:bg-white/5 transition-all"
          title="GitHub 仓库"
        >
          <Github class="w-5 h-5" />
        </a>

        <!-- 移动端汉堡菜单按钮 -->
        <button
          @click="mobileMenuOpen = !mobileMenuOpen"
          class="md:hidden p-2 rounded-xl text-slate-500 hover:text-slate-900 hover:bg-pink-100/50 dark:text-slate-400 dark:hover:text-white dark:hover:bg-white/5"
        >
          <Menu v-if="!mobileMenuOpen" class="w-5 h-5" />
          <X v-else class="w-5 h-5" />
        </button>
      </div>
    </div>

    <!-- 移动端抽屉菜单 -->
    <div
      v-if="mobileMenuOpen"
      class="md:hidden fixed inset-x-0 top-[65px] bg-white/95 dark:bg-space-950/95 backdrop-blur-2xl border-b border-pink-200/50 dark:border-white/10 p-5 space-y-3 z-50 shadow-xl"
    >
      <router-link
        v-for="item in navItems"
        :key="item.path"
        :to="item.path"
        @click="mobileMenuOpen = false"
        class="flex items-center space-x-3 px-4 py-3 rounded-xl text-slate-700 dark:text-slate-300 hover:bg-pink-50 dark:hover:bg-white/5 transition-all"
        :class="{ 'bg-pink-100/80 text-pink-600 dark:bg-white/10 dark:text-nebula-cyan font-bold': $route.path === item.path }"
      >
        <component :is="item.icon" class="w-5 h-5 text-pink-600 dark:text-nebula-cyan" />
        <span>{{ item.title }}</span>
      </router-link>
    </div>

    <!-- 星轨列车长彩蛋暗门弹窗 -->
    <ConductorModal v-model="showConductorModal" />
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { Home, BookOpen, MessageSquare, Camera, User, Github, Music, Menu, X, Disc3, Sun, Moon } from 'lucide-vue-next';
import { useMusicStore } from '@/stores/music';
import { useThemeStore } from '@/stores/theme';
import ConductorModal from '@/components/ConductorModal.vue';

const musicStore = useMusicStore();
const themeStore = useThemeStore();
const isScrolled = ref(false);
const mobileMenuOpen = ref(false);
const showConductorModal = ref(false);

// 三击帕姆头像彩蛋检测
let clickCount = 0;
let clickTimer: any = null;

const handleLogoClick = (e: MouseEvent) => {
  clickCount++;
  if (clickTimer) clearTimeout(clickTimer);

  if (clickCount >= 3) {
    e.preventDefault();
    clickCount = 0;
    showConductorModal.value = true;
    return;
  }

  clickTimer = setTimeout(() => {
    clickCount = 0;
  }, 1000);
};

const navItems = [
  { title: '首页', path: '/', icon: Home },
  { title: '文章', path: '/posts', icon: BookOpen },
  { title: '星穹音乐', path: '/music', icon: Disc3 },
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
