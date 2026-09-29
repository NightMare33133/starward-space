<template>
  <div class="max-w-4xl mx-auto px-4 sm:px-6 py-10 space-y-12">
    <!-- 头部个人名片 -->
    <div class="glass-card rounded-3xl p-6 sm:p-10 border border-pink-200/50 dark:border-white/10 relative overflow-hidden shadow-xl">
      <div class="flex flex-col sm:flex-row items-center sm:items-start gap-6 relative z-10">
        <!-- 头像 -->
        <div class="w-24 h-24 sm:w-28 sm:h-28 rounded-2xl overflow-hidden p-[2px] bg-gradient-to-tr from-pink-500 via-rose-400 to-nebula-cyan dark:from-nebula-cyan dark:to-nebula-purple shadow-xl shrink-0">
          <div class="w-full h-full rounded-[14px] overflow-hidden bg-pink-100 dark:bg-space-900">
            <img
              src="/images/avatar.png"
              alt="NightMare33133"
              class="w-full h-full object-cover"
            />
          </div>
        </div>

        <div class="space-y-3 text-center sm:text-left flex-1">
          <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-2">
            <div>
              <h1 class="text-2xl font-extrabold text-slate-800 dark:text-white">NightMare33133</h1>
              <p class="text-xs text-pink-600 dark:text-nebula-cyan font-mono mt-0.5">Software Engineer · AI & Systems Explorer</p>
            </div>
            <a
              href="https://github.com/NightMare33133"
              target="_blank"
              class="inline-flex items-center space-x-1.5 px-3.5 py-1.5 rounded-full text-xs glass-card hover:bg-pink-100 dark:hover:bg-white/10 text-slate-700 dark:text-slate-300 transition-colors border border-pink-200/60 dark:border-white/10 mx-auto sm:mx-0 shadow-sm"
            >
              <Github class="w-3.5 h-3.5" />
              <span>GitHub Profile</span>
            </a>
          </div>

          <p class="text-xs sm:text-sm text-slate-600 dark:text-slate-300 leading-relaxed">
            探索以严谨的工程思维与纯粹的极客浪漫构建软件系统。
            热衷于深挖现代后端架构底座、打磨高颜值前端界面、以及探索前沿 AI 智能体生态。
          </p>
        </div>
      </div>
    </div>

    <!-- 求学与成长轨迹 (Timeline) -->
    <div class="space-y-4">
      <h2 class="text-lg font-bold text-slate-800 dark:text-white flex items-center space-x-2">
        <GraduationCap class="w-5 h-5 text-pink-600 dark:text-nebula-cyan" />
        <span>求学与航迹 · Journey</span>
      </h2>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div class="glass-card rounded-2xl p-5 border border-pink-200/50 dark:border-white/10 space-y-2">
          <div class="flex items-center justify-between">
            <span class="text-xs font-mono text-pink-600 dark:text-nebula-cyan font-semibold">2025 - 2027</span>
            <span class="px-2 py-0.5 rounded text-[10px] bg-pink-100 text-pink-700 border border-pink-200 dark:bg-nebula-cyan/10 dark:text-nebula-cyan dark:border-nebula-cyan/20">硕士研究生</span>
          </div>
          <h3 class="text-base font-bold text-slate-800 dark:text-white">香港城市大学（东莞）</h3>
          <p class="text-xs text-slate-600 dark:text-slate-400">商务资讯系统 (BIS) · 理学硕士</p>
          <p class="text-[11px] text-slate-500 font-mono">✦ 阶段联合培养 · 复旦大学</p>
        </div>

        <div class="glass-card rounded-2xl p-5 border border-pink-200/50 dark:border-white/10 space-y-2">
          <div class="flex items-center justify-between">
            <span class="text-xs font-mono text-slate-500 dark:text-slate-400 font-semibold">2021 - 2025</span>
            <span class="px-2 py-0.5 rounded text-[10px] bg-slate-100 text-slate-700 border border-slate-200 dark:bg-white/5 dark:text-slate-400 dark:border-white/10">本科</span>
          </div>
          <h3 class="text-base font-bold text-slate-800 dark:text-white">福州大学</h3>
          <p class="text-xs text-slate-600 dark:text-slate-400">计算机与大数据学院 · 计算机科学与技术</p>
        </div>
      </div>
    </div>

    <!-- 技术栈矩阵 (Tech Matrix) -->
    <div class="space-y-4">
      <h2 class="text-lg font-bold text-slate-800 dark:text-white flex items-center space-x-2">
        <Code2 class="w-5 h-5 text-purple-600 dark:text-nebula-purple" />
        <span>技术栈矩阵 · Tech Stack</span>
      </h2>

      <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div class="glass-card rounded-2xl p-5 border border-pink-200/50 dark:border-white/10 space-y-3">
          <h3 class="text-xs font-mono uppercase tracking-wider text-slate-500 dark:text-slate-400 font-bold">Backend & DB</h3>
          <div class="flex flex-wrap gap-1.5">
            <span v-for="t in ['Java 21', 'Spring Boot 3', 'MyBatis', 'MySQL 8.4', 'Redis', 'RESTful API']" :key="t" class="px-2.5 py-1 rounded-lg text-xs bg-pink-100/70 text-slate-700 border border-pink-200/60 dark:bg-white/5 dark:text-slate-300 dark:border-white/5">
              {{ t }}
            </span>
          </div>
        </div>

        <div class="glass-card rounded-2xl p-5 border border-pink-200/50 dark:border-white/10 space-y-3">
          <h3 class="text-xs font-mono uppercase tracking-wider text-slate-500 dark:text-slate-400 font-bold">Frontend & UI</h3>
          <div class="flex flex-wrap gap-1.5">
            <span v-for="t in ['Vue 3', 'Vite 8', 'TypeScript', 'Tailwind CSS', 'Pinia', 'HTML5 Canvas']" :key="t" class="px-2.5 py-1 rounded-lg text-xs bg-pink-100/70 text-slate-700 border border-pink-200/60 dark:bg-white/5 dark:text-slate-300 dark:border-white/5">
              {{ t }}
            </span>
          </div>
        </div>

        <div class="glass-card rounded-2xl p-5 border border-pink-200/50 dark:border-white/10 space-y-3">
          <h3 class="text-xs font-mono uppercase tracking-wider text-slate-500 dark:text-slate-400 font-bold">AI & DevOps</h3>
          <div class="flex flex-wrap gap-1.5">
            <span v-for="t in ['AI Agents', 'OpenAPI 3', 'Git / GitHub', 'Docker', 'Linux / zsh', 'Nginx']" :key="t" class="px-2.5 py-1 rounded-lg text-xs bg-pink-100/70 text-slate-700 border border-pink-200/60 dark:bg-white/5 dark:text-slate-300 dark:border-white/5">
              {{ t }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { GraduationCap, Code2, Github } from 'lucide-vue-next';
</script>
