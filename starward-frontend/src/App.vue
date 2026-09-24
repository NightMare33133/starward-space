<template>
  <div class="min-h-screen bg-space-950 text-slate-100 flex flex-col relative selection:bg-nebula-cyan/30 selection:text-white">
    <!-- 动态微光星空粒子画布 -->
    <StarBackground />

    <!-- 顶部玻璃导航栏 -->
    <Navbar />

    <!-- 路由主视口与页面平滑过渡 -->
    <main class="flex-1 relative z-10 pt-16">
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

    <!-- 网易云悬浮音乐岛 -->
    <MusicPlayer />

    <!-- 页脚 -->
    <Footer />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import StarBackground from '@/components/StarBackground.vue';
import Navbar from '@/components/Navbar.vue';
import MusicPlayer from '@/components/MusicPlayer.vue';
import Footer from '@/components/Footer.vue';
import { useThemeStore } from '@/stores/theme';

const themeStore = useThemeStore();

onMounted(() => {
  themeStore.initTheme();
});
</script>
