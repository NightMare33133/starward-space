<template>
  <div class="fixed top-5 inset-x-0 z-[99999] flex flex-col items-center pointer-events-none space-y-2.5 px-4 select-none">
    <transition-group
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="transform -translate-y-4 opacity-0 scale-95"
      enter-to-class="transform translate-y-0 opacity-100 scale-100"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="transform translate-y-0 opacity-100 scale-100"
      leave-to-class="transform -translate-y-2 opacity-0 scale-95"
    >
      <div
        v-for="item in toasts"
        :key="item.id"
        class="pointer-events-auto flex items-center space-x-2.5 px-4 py-2.5 rounded-2xl shadow-2xl backdrop-blur-xl border text-xs font-medium max-w-md transition-all duration-300"
        :class="toastStyle(item.type)"
      >
        <!-- 图标 -->
        <component :is="toastIcon(item.type)" class="w-4 h-4 shrink-0 animate-pulse" />

        <!-- 文本内容 -->
        <span class="leading-relaxed tracking-wide text-slate-100">{{ item.message }}</span>

        <!-- 手动关闭按钮 -->
        <button
          @click="remove(item.id)"
          class="p-0.5 rounded-full hover:bg-white/10 text-slate-400 hover:text-white transition-colors ml-1"
        >
          <X class="w-3.5 h-3.5" />
        </button>
      </div>
    </transition-group>
  </div>
</template>

<script setup lang="ts">
import { CheckCircle2, AlertCircle, AlertTriangle, Sparkles, X } from 'lucide-vue-next';
import { useToast } from '@/composables/useToast';

const { toasts, remove } = useToast();

const toastIcon = (type: string) => {
  switch (type) {
    case 'success':
      return CheckCircle2;
    case 'error':
      return AlertCircle;
    case 'warning':
      return AlertTriangle;
    default:
      return Sparkles;
  }
};

const toastStyle = (type: string) => {
  switch (type) {
    case 'success':
      return 'bg-space-900/90 border-emerald-500/40 text-emerald-300 shadow-emerald-500/15';
    case 'error':
      return 'bg-space-900/90 border-rose-500/40 text-rose-300 shadow-rose-500/15';
    case 'warning':
      return 'bg-space-900/90 border-amber-500/40 text-amber-300 shadow-amber-500/15';
    default:
      return 'bg-space-900/90 border-nebula-cyan/40 text-nebula-cyan shadow-nebula-cyan/15';
  }
};
</script>
