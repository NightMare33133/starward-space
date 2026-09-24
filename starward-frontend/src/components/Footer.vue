<template>
  <footer class="mt-24 border-t border-white/10 bg-space-950/60 backdrop-blur-md py-12 relative z-10">
    <div class="max-w-6xl mx-auto px-4 sm:px-6 flex flex-col md:flex-row items-center justify-between gap-6">
      <!-- 左侧版权与航行时间 -->
      <div class="space-y-2 text-center md:text-left">
        <div class="flex items-center justify-center md:justify-start space-x-2">
          <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
          <span class="text-sm font-semibold tracking-wider text-slate-200">STARWARD SPACE · 星向空间</span>
        </div>
        <p class="text-xs text-slate-500 font-mono">
          © {{ currentYear }} Designed & Built with ❤️ by NightMare33133.
        </p>
        <p class="text-xs text-slate-500 font-mono">
          🚀 本站已在数字星海漫游 <span class="text-nebula-cyan font-semibold">{{ runningDays }}</span> 天
        </p>
      </div>

      <!-- 右侧开源协议与技术栈标识 -->
      <div class="flex flex-col items-center md:items-end space-y-2">
        <div class="flex items-center space-x-4 text-xs text-slate-400">
          <a
            href="https://github.com/NightMare33133/starward-space/blob/main/LICENSE"
            target="_blank"
            class="hover:text-nebula-cyan transition-colors underline underline-offset-4"
          >
            Apache 2.0 License
          </a>
          <span>·</span>
          <a
            href="https://github.com/NightMare33133/starward-space"
            target="_blank"
            class="hover:text-nebula-cyan transition-colors"
          >
            GitHub 源码
          </a>
        </div>
        <p class="text-[11px] text-slate-600 font-mono">
          Powered by Vue 3 · Vite · Spring Boot 3 · Java 21 · MySQL 8.4
        </p>
      </div>
    </div>
  </footer>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const currentYear = new Date().getFullYear();

// 计算建站航行天数（以 2024-09-01 或当前基准）
const startDate = new Date('2024-09-01').getTime();
const runningDays = computed(() => {
  const diff = Date.now() - startDate;
  return Math.max(1, Math.floor(diff / (1000 * 60 * 60 * 24)));
});
</script>
