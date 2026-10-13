<template>
  <header class="fixed top-0 left-0 right-0 z-40 transition-all duration-300" :class="{ 'py-3 backdrop-blur-xl bg-white/75 dark:bg-space-950/70 border-b border-pink-200/40 dark:border-white/10 shadow-lg shadow-pink-500/5 dark:shadow-none': isScrolled, 'py-5 bg-transparent': !isScrolled }">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 flex items-center justify-between">
      <!-- 品牌标识 Logo (帕姆列车长) - 支持三击触发星轨密令彩蛋 -->
      <router-link to="/" class="flex items-center space-x-3 group" @click="handleLogoClick">
        <div class="w-10 h-10 rounded-2xl bg-gradient-to-tr from-amber-400 via-nebula-pink to-nebula-cyan p-[1.5px] shadow-lg shadow-amber-500/25 group-hover:scale-110 active:scale-95 transition-transform overflow-hidden shrink-0 cursor-pointer">
          <div class="w-full h-full rounded-[14px] overflow-hidden bg-space-950">
            <img
              src="/images/pompom.webp"
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
      <nav class="hidden md:flex items-center space-x-1 glass-card px-3 py-1.5 rounded-full border border-pink-200/40 dark:border-white/10 shrink-0">
        <template v-for="item in navItems" :key="item.title">
          <!-- 普通路由链接 -->
          <router-link
            v-if="!item.children"
            :to="item.path!"
            class="px-3.5 py-1.5 rounded-full text-sm font-medium transition-all duration-200 whitespace-nowrap"
            :class="$route.path === item.path ? 'text-pink-600 bg-pink-100/70 dark:text-white dark:bg-white/10 shadow-sm font-semibold' : 'text-slate-600 hover:text-pink-600 hover:bg-pink-50/60 dark:text-slate-400 dark:hover:text-slate-100 dark:hover:bg-white/5'"
          >
            <div class="flex items-center space-x-1.5">
              <component :is="item.icon" class="w-4 h-4 shrink-0" />
              <span>{{ item.title }}</span>
            </div>
          </router-link>

          <!-- 具有二级菜单的下拉项（如：追番观影、星际漫游） -->
          <div
            v-else
            class="relative group"
          >
            <button
              class="px-3.5 py-1.5 rounded-full text-sm font-medium transition-all duration-200 flex items-center space-x-1.5 cursor-pointer whitespace-nowrap"
              :class="isSubRouteActive(item) ? 'text-pink-600 bg-pink-100/70 dark:text-white dark:bg-white/10 shadow-sm font-semibold' : 'text-slate-600 hover:text-pink-600 hover:bg-pink-50/60 dark:text-slate-400 dark:hover:text-slate-100 dark:hover:bg-white/5'"
            >
              <component :is="item.icon" class="w-4 h-4 shrink-0" />
              <span>{{ item.title }}</span>
              <ChevronDown class="w-3.5 h-3.5 transition-transform duration-200 group-hover:rotate-180 opacity-70 shrink-0" />
            </button>

            <!-- 下拉浮动层 -->
            <div
              class="absolute top-full left-1/2 -translate-x-1/2 pt-2 opacity-0 pointer-events-none group-hover:opacity-100 group-hover:pointer-events-auto transition-all duration-200 z-50 min-w-[210px]"
            >
              <div class="glass-card rounded-2xl p-2 shadow-2xl border border-pink-200/50 dark:border-white/10 bg-white/95 dark:bg-space-950/95 backdrop-blur-2xl flex flex-col space-y-1">
                <router-link
                  v-for="sub in item.children"
                  :key="sub.path"
                  :to="sub.path"
                  class="flex items-center space-x-2.5 px-3 py-2 rounded-xl text-xs transition-all"
                  :class="$route.path === sub.path ? 'text-pink-600 bg-pink-100/80 dark:text-nebula-cyan dark:bg-white/10 font-bold' : 'text-slate-700 hover:text-pink-600 hover:bg-pink-50/70 dark:text-slate-300 dark:hover:bg-white/5'"
                >
                  <div class="w-7 h-7 rounded-lg flex items-center justify-center shrink-0" :class="$route.path === sub.path ? 'bg-pink-500/10 dark:bg-nebula-cyan/10 text-pink-600 dark:text-nebula-cyan' : 'bg-slate-100 dark:bg-white/5 text-slate-500 dark:text-slate-400'">
                    <component :is="sub.icon" class="w-4 h-4" />
                  </div>
                  <div class="flex flex-col text-left">
                    <span class="font-medium text-xs">{{ sub.title }}</span>
                    <span class="text-[10px] text-slate-400 dark:text-slate-500">{{ sub.desc }}</span>
                  </div>
                </router-link>
              </div>
            </div>
          </div>
        </template>
      </nav>

      <!-- 右侧操作区：主题切换、音乐岛触发器、GitHub 仓库 -->
      <div class="flex items-center space-x-2">
        <!-- 日夜沉浸式主题切换按钮 -->
        <button
          @click="themeStore.toggleTheme"
          class="relative w-9 h-9 rounded-xl transition-all duration-300 flex items-center justify-center group active:scale-90"
          :class="themeStore.isDark ? 'text-amber-300 hover:text-amber-200 hover:bg-white/10' : 'text-pink-600 hover:text-pink-700 hover:bg-pink-100/60'"
          :title="themeStore.isDark ? '切换至日间模式 · 落樱晨曦' : '切换至夜间模式 · 深空流萤'"
        >
          <!-- 瞬态高能光环冲击波 (仅在切换那一下爆发) -->
          <span
            v-if="themeStore.burstCount > 0"
            :key="'shockwave-' + themeStore.burstCount"
            class="absolute inset-0 rounded-xl pointer-events-none animate-theme-shockwave border-2"
            :class="themeStore.isDark ? 'border-amber-400 shadow-[0_0_12px_rgba(251,191,36,0.6)]' : 'border-pink-500 shadow-[0_0_12px_rgba(244,114,182,0.6)]'"
          ></span>

          <!-- 瞬态星光射线绽放 (Starburst Spark) -->
          <span
            v-if="themeStore.burstCount > 0"
            :key="'spark-' + themeStore.burstCount"
            class="absolute w-10 h-10 pointer-events-none animate-theme-spark flex items-center justify-center"
          >
            <span
              class="w-1.5 h-1.5 rounded-full"
              :class="themeStore.isDark ? 'bg-amber-300 shadow-[0_0_8px_#fde047]' : 'bg-pink-400 shadow-[0_0_8px_#f472b6]'"
            ></span>
          </span>

          <!-- 图标 3D 弹簧弹性翻转 -->
          <transition name="theme-icon" mode="out-in">
            <Sun v-if="themeStore.isDark" key="sun" class="w-5 h-5 group-hover:rotate-45 transition-transform duration-300" />
            <Moon v-else key="moon" class="w-5 h-5 group-hover:-rotate-12 transition-transform duration-300" />
          </transition>
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
      class="md:hidden fixed inset-x-0 top-[65px] bg-white/95 dark:bg-space-950/95 backdrop-blur-2xl border-b border-pink-200/50 dark:border-white/10 p-5 space-y-2 z-50 shadow-xl max-h-[calc(100vh-80px)] overflow-y-auto"
    >
      <template v-for="item in navItems" :key="item.title">
        <!-- 普通单级项 -->
        <router-link
          v-if="!item.children"
          :to="item.path!"
          @click="mobileMenuOpen = false"
          class="flex items-center space-x-3 px-4 py-2.5 rounded-xl text-slate-700 dark:text-slate-300 hover:bg-pink-50 dark:hover:bg-white/5 transition-all text-sm"
          :class="{ 'bg-pink-100/80 text-pink-600 dark:bg-white/10 dark:text-nebula-cyan font-bold': $route.path === item.path }"
        >
          <component :is="item.icon" class="w-5 h-5 text-pink-600 dark:text-nebula-cyan" />
          <span>{{ item.title }}</span>
        </router-link>

        <!-- 二级分组项（追番观影） -->
        <div v-else class="rounded-xl bg-pink-50/40 dark:bg-white/[0.02] p-2 space-y-1">
          <div class="flex items-center space-x-2.5 px-3 py-1.5 text-xs font-semibold text-slate-500 dark:text-slate-400">
            <component :is="item.icon" class="w-4 h-4 text-pink-500 dark:text-nebula-cyan" />
            <span>{{ item.title }}</span>
          </div>
          <div class="pl-3 space-y-1">
            <router-link
              v-for="sub in item.children"
              :key="sub.path"
              :to="sub.path"
              @click="mobileMenuOpen = false"
              class="flex items-center justify-between px-3 py-2 rounded-lg text-xs transition-all"
              :class="$route.path === sub.path ? 'bg-pink-100/80 text-pink-600 dark:bg-white/10 dark:text-nebula-cyan font-bold' : 'text-slate-600 hover:bg-pink-50 dark:text-slate-300 dark:hover:bg-white/5'"
            >
              <div class="flex items-center space-x-2">
                <component :is="sub.icon" class="w-4 h-4 text-pink-500 dark:text-nebula-cyan" />
                <span>{{ sub.title }}</span>
              </div>
              <span class="text-[10px] text-slate-400 dark:text-slate-500">{{ sub.desc }}</span>
            </router-link>
          </div>
        </div>
      </template>
    </div>

    <!-- 星轨列车长彩蛋暗门弹窗 -->
    <ConductorModal v-model="showConductorModal" />
  </header>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router';
import {
  Home, BookOpen, MessageSquare, Camera, User, Github, Music,
  Menu, X, Disc3, Sun, Moon, Clapperboard, Tv, Sparkles, ChevronDown, Compass
} from 'lucide-vue-next';
import { useMusicStore } from '@/stores/music';
import { useThemeStore } from '@/stores/theme';
import ConductorModal from '@/components/ConductorModal.vue';

const route = useRoute();
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

const isSubRouteActive = (item: any) => {
  if (item.path) return route.path === item.path;
  if (item.children) {
    return item.children.some((c: any) => route.path === c.path);
  }
  return false;
};

const navItems = [
  { title: '首页', path: '/', icon: Home },
  { title: '文章', path: '/posts', icon: BookOpen },
  {
    title: '追番观影',
    icon: Clapperboard,
    children: [
      { title: 'B 站放映厅', path: '/bilibili', desc: '哔哩哔哩个人追番与追剧', icon: Tv },
      { title: 'Bangumi 番组', path: '/bangumi', desc: 'bgm.tv 动画与游戏评分标记', icon: Sparkles }
    ]
  },
  {
    title: '星际漫游',
    icon: Compass,
    children: [
      { title: '摄影视界', path: '/gallery', desc: '现实光影漫游与快门定格', icon: Camera },
      { title: '星际碎语', path: '/moments', desc: '微动态、日常碎片与灵感', icon: MessageSquare },
      { title: '星穹音乐', path: '/music', desc: '车载黑胶唱片与常驻歌单', icon: Disc3 }
    ]
  },
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
