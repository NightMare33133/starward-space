<template>
  <div class="min-h-screen bg-space-950 text-slate-100 flex flex-col relative selection:bg-nebula-cyan/30 selection:text-white">
    <!-- 全局星际轻提示组件 (Toast) -->
    <ToastContainer />

    <!-- 动态微光星空粒子画布 -->
    <StarBackground />

    <!-- 顶部玻璃导航栏 (仅在前台非管理路由展示，防止后台出现双导航栏) -->
    <Navbar v-if="!isAdminRoute" />

    <!-- 路由主视口与页面平滑过渡 -->
    <main class="flex-1 relative z-10" :class="{ 'pt-16': !isAdminRoute }">
      <router-view v-slot="{ Component }">
        <transition
          enter-active-class="transition duration-300 ease-out"
          enter-from-class="opacity-0 translate-y-3"
          enter-to-class="opacity-100 translate-y-0"
          leave-active-class="transition duration-150 ease-in"
          leave-from-class="opacity-100 translate-y-0"
          leave-to-class="opacity-0 -translate-y-3"
          mode="out-in"
        >
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 网易云悬浮音乐岛 (全站常驻) -->
    <MusicPlayer />

    <!-- 页脚 (仅在前台展示) -->
    <Footer v-if="!isAdminRoute" />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import StarBackground from '@/components/StarBackground.vue';
import Navbar from '@/components/Navbar.vue';
import MusicPlayer from '@/components/MusicPlayer.vue';
import Footer from '@/components/Footer.vue';
import ToastContainer from '@/components/ToastContainer.vue';
import { useThemeStore } from '@/stores/theme';

const route = useRoute();
const themeStore = useThemeStore();

// 判断是否为后台管理路由 (/admin/**)
const isAdminRoute = computed(() => route.path.startsWith('/admin'));

onMounted(() => {
  themeStore.initTheme();
});
</script>
