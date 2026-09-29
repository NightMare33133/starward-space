<template>
  <footer class="mt-20 border-t border-pink-200/40 dark:border-white/10 bg-white/60 dark:bg-space-950/60 backdrop-blur-md pt-10 pb-20 md:pb-14 relative z-10 transition-colors duration-500">
    <div class="max-w-6xl mx-auto px-4 sm:px-6">
      <div class="flex flex-col md:flex-row items-center justify-between gap-6 pb-6 border-b border-pink-200/20 dark:border-white/5">
        <!-- 左侧版权与航行时间 -->
        <div class="space-y-2 text-center md:text-left">
          <div class="flex items-center justify-center md:justify-start space-x-2">
            <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
            <span class="text-sm font-semibold tracking-wider text-slate-800 dark:text-slate-200">STARWARD · starlight の 星向空间</span>
          </div>
          <p class="text-xs text-slate-500 font-mono">
            © {{ currentYear }} Designed & Built with ❤️ by NightMare33133.
          </p>
          <p class="text-xs text-slate-500 font-mono">
            🚀 本站已在数字星海漫游 <span class="text-pink-600 dark:text-nebula-cyan font-semibold">{{ runningDays }}</span> 天
          </p>
        </div>

        <!-- 右侧开源协议与技术栈标识 -->
        <div class="flex flex-col items-center md:items-end space-y-2 text-center md:text-right">
          <div class="flex items-center space-x-4 text-xs text-slate-500 dark:text-slate-400">
            <a
              href="https://github.com/NightMare33133/starward-space/blob/main/LICENSE"
              target="_blank"
              class="hover:text-pink-600 dark:hover:text-nebula-cyan transition-colors underline underline-offset-4"
            >
              Apache 2.0 License
            </a>
            <span>·</span>
            <a
              href="https://github.com/NightMare33133/starward-space"
              target="_blank"
              class="hover:text-pink-600 dark:hover:text-nebula-cyan transition-colors"
            >
              GitHub 源码
            </a>
            <span>·</span>
            <router-link
              to="/admin"
              class="hover:text-amber-500 transition-colors flex items-center space-x-1"
              title="星轨控制中枢"
            >
              <span>✦ 控制中枢</span>
            </router-link>
          </div>
          <p class="text-[11px] text-slate-400 dark:text-slate-600 font-mono">
            Powered by Vue 3 · Vite · Spring Boot 3 · Java 21 · MySQL 8.4
          </p>
        </div>
      </div>

      <!-- 页脚底部：正中居中醒目展示萌备徽章与免责声明，完全避开右下角悬浮音乐播放器 -->
      <div class="pt-6 flex flex-col items-center justify-center space-y-3 text-center">
        <a
          href="https://icp.gov.moe/?keyword=20263313"
          target="_blank"
          rel="noopener noreferrer"
          class="inline-flex items-center space-x-1.5 px-3.5 py-1.5 rounded-full text-xs font-mono font-medium transition-all duration-300 border bg-pink-50 text-pink-600 border-pink-200 hover:bg-pink-100 hover:border-pink-300 hover:scale-105 dark:bg-pink-500/10 dark:text-pink-300 dark:border-pink-500/25 dark:hover:bg-pink-500/20 dark:hover:border-pink-400/40 shadow-sm"
          title="萌国ICP备案认证"
        >
          <span>🌸</span>
          <span>萌ICP备20263313号</span>
        </a>
        <p class="text-[11px] font-mono text-slate-400 dark:text-slate-600">
          All Star Rail visual assets belong to miHoYo / HoYoverse · Non-commercial personal blog
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
