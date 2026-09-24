<template>
  <div class="max-w-3xl mx-auto px-4 sm:px-6 py-10 space-y-8">
    <!-- 头部说明区 -->
    <div class="space-y-3 border-b border-white/10 pb-6">
      <h1 class="text-3xl font-extrabold text-white tracking-tight flex items-center space-x-3">
        <Sparkles class="w-8 h-8 text-nebula-purple" />
        <span>星际碎语 · Moments</span>
      </h1>
      <p class="text-sm text-slate-400">
        记录瞬间的灵感、深夜敲代码的碎碎念、以及生活的细碎光芒。
      </p>
    </div>

    <!-- 碎语发布面板 (极简毛玻璃卡片) -->
    <div class="glass-card rounded-2xl p-5 border border-white/10 space-y-4">
      <textarea
        v-model="newContent"
        rows="3"
        placeholder="这一刻在想什么？发送一条星际电波吧..."
        class="w-full bg-space-950/60 border border-white/10 rounded-xl p-3 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-nebula-purple/50 transition-colors resize-none"
      ></textarea>

      <div class="flex flex-wrap items-center justify-between gap-3 pt-1">
        <!-- 心情与定位 -->
        <div class="flex items-center space-x-3">
          <!-- 心情 Emoji 快速选择 -->
          <div class="flex items-center space-x-1 bg-white/5 rounded-lg p-1 border border-white/5">
            <button
              v-for="emoji in moodOptions"
              :key="emoji"
              @click="selectedMood = emoji"
              class="w-7 h-7 rounded flex items-center justify-center text-sm transition-all"
              :class="selectedMood === emoji ? 'bg-nebula-purple/30 scale-110 shadow-sm' : 'hover:bg-white/10 opacity-70 hover:opacity-100'"
            >
              {{ emoji }}
            </button>
          </div>

          <!-- 地点 -->
          <div class="flex items-center space-x-1 text-xs text-slate-400 bg-white/5 px-2.5 py-1.5 rounded-lg border border-white/5">
            <MapPin class="w-3.5 h-3.5 text-nebula-pink" />
            <input
              v-model="customLocation"
              type="text"
              class="bg-transparent border-none outline-none text-xs text-slate-300 w-20"
              placeholder="地点"
            />
          </div>
        </div>

        <!-- 发射按钮 -->
        <button
          @click="handlePublish"
          :disabled="publishing || !newContent.trim()"
          class="px-5 py-2 rounded-xl bg-gradient-to-r from-nebula-purple to-nebula-pink text-white text-xs font-semibold shadow-lg shadow-nebula-purple/20 hover:shadow-nebula-purple/35 hover:scale-105 active:scale-95 disabled:opacity-50 disabled:pointer-events-none transition-all flex items-center space-x-1.5"
        >
          <Send class="w-3.5 h-3.5" />
          <span>{{ publishing ? '发射中...' : '发射电波' }}</span>
        </button>
      </div>

      <div v-if="postError" class="text-xs text-rose-400 font-mono">
        {{ postError }}
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="text-center py-16 text-slate-500 text-sm">
      <div class="inline-block w-6 h-6 border-2 border-nebula-purple border-t-transparent rounded-full animate-spin mb-2"></div>
      <p>正在接收历史星际信号...</p>
    </div>

    <!-- 碎语时间线列表 -->
    <div v-else class="space-y-6 relative before:absolute before:inset-0 before:left-5 before:w-0.5 before:bg-white/10">
      <div
        v-for="moment in moments"
        :key="moment.id"
        class="relative flex items-start space-x-4 group"
      >
        <!-- 时间线锚点 -->
        <div class="w-10 h-10 rounded-full glass-card border border-white/20 flex items-center justify-center text-lg shrink-0 shadow-lg group-hover:border-nebula-purple/60 group-hover:scale-110 transition-all z-10 bg-space-950">
          {{ moment.moodEmoji || '✨' }}
        </div>

        <!-- 碎语卡片内容 -->
        <div class="flex-1 glass-card rounded-2xl p-5 border border-white/10 space-y-3 group-hover:border-nebula-purple/30 transition-all">
          <div class="flex items-center justify-between text-xs text-slate-500 font-mono">
            <span v-if="moment.location" class="flex items-center space-x-1 text-slate-400">
              <MapPin class="w-3 h-3 text-nebula-pink" />
              <span>{{ moment.location }}</span>
            </span>
            <span v-else>星向电台</span>
            <span>{{ formatTime(moment.createdAt) }}</span>
          </div>

          <p class="text-sm text-slate-200 leading-relaxed whitespace-pre-wrap">
            {{ moment.content }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { Sparkles, MapPin, Send } from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { getMoments, createMoment } from '@/api/moments';
import type { Moment } from '@/types';

const moments = ref<Moment[]>([]);
const loading = ref(true);
const publishing = ref(false);
const postError = ref('');

const newContent = ref('');
const selectedMood = ref('🚀');
const customLocation = ref('福建石狮');
const moodOptions = ['🚀', '🌟', '☕️', '🪐', '💻', '🌸'];

const formatTime = (dateStr: string) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`;
};

const loadMoments = async () => {
  try {
    moments.value = await getMoments();
  } catch (err) {
    console.error('Failed to load moments:', err);
  } finally {
    loading.value = false;
  }
};

const handlePublish = async () => {
  if (!newContent.value.trim()) return;
  publishing.value = true;
  postError.value = '';

  try {
    await createMoment({
      content: newContent.value.trim(),
      mood: selectedMood.value,
    });
    // 礼花特效
    confetti({
      particleCount: 50,
      spread: 60,
      origin: { y: 0.8 },
      colors: ['#38bdf8', '#a855f7', '#ec4899']
    });
    newContent.value = '';
    await loadMoments();
  } catch (err: any) {
    postError.value = err.message || '发布失败';
  } finally {
    publishing.value = false;
  }
};

onMounted(() => {
  loadMoments();
});
</script>
