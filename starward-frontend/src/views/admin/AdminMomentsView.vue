<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <!-- 左侧：发射台卡片 -->
      <div class="glass-card p-6 rounded-2xl border border-white/10 space-y-4">
        <div class="flex items-center space-x-2">
          <div class="p-2 rounded-xl bg-nebula-cyan/10 text-nebula-cyan border border-nebula-cyan/20">
            <Send class="w-4 h-4" />
          </div>
          <div>
            <h3 class="text-sm font-bold text-white">星际碎语速发发射台</h3>
            <p class="text-[11px] text-slate-400">一键将生活随笔与灵感广播至展示端</p>
          </div>
        </div>

        <form @submit.prevent="handlePublish" class="space-y-4">
          <!-- 心情 Emoji 快速点选 -->
          <div>
            <label class="block text-xs font-medium text-slate-300 mb-1.5 flex items-center justify-between">
              <span>当刻星际心境</span>
              <span class="text-base">{{ selectedEmoji }}</span>
            </label>
            <div class="flex flex-wrap gap-2">
              <button
                v-for="item in moodOptions"
                :key="item.emoji"
                type="button"
                @click="selectedEmoji = item.emoji"
                class="px-2.5 py-1 rounded-xl text-xs border transition-all flex items-center space-x-1"
                :class="selectedEmoji === item.emoji ? 'bg-amber-400/20 border-amber-400 text-white shadow' : 'bg-space-950/80 border-white/10 text-slate-400 hover:text-white'"
              >
                <span>{{ item.emoji }}</span>
                <span class="text-[10px]">{{ item.label }}</span>
              </button>
            </div>
          </div>

          <!-- 地点 / 空间坐标 -->
          <div>
            <label class="block text-xs font-medium text-slate-300 mb-1">
              空间坐标 (Location)
            </label>
            <div class="relative">
              <MapPin class="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-slate-500" />
              <input
                v-model="locationInput"
                type="text"
                placeholder="例如：星穹列车 · 观景车厢"
                class="w-full pl-8 pr-3 py-2 rounded-xl bg-space-950/80 border border-white/10 text-xs text-slate-100 placeholder-slate-500 focus:outline-none focus:border-amber-400/80 transition-all font-mono"
              />
            </div>
            <!-- 预设地点 -->
            <div class="flex flex-wrap gap-1.5 mt-1.5">
              <button
                v-for="loc in presetLocations"
                :key="loc"
                type="button"
                @click="locationInput = loc"
                class="text-[10px] text-slate-500 hover:text-nebula-cyan transition-colors"
              >
                #{{ loc }}
              </button>
            </div>
          </div>

          <!-- 正文 -->
          <div>
            <label class="block text-xs font-medium text-slate-300 mb-1">
              碎语正文 <span class="text-rose-400">*</span>
            </label>
            <textarea
              v-model="content"
              rows="4"
              placeholder="分享此时此刻的技术突破、列车航行记录或摸鱼瞬间..."
              class="w-full px-3.5 py-2.5 rounded-xl bg-space-950/80 border border-white/10 text-xs text-slate-100 placeholder-slate-500 focus:outline-none focus:border-amber-400/80 transition-all leading-relaxed"
            ></textarea>
          </div>

          <!-- 发射按钮 -->
          <button
            type="submit"
            :disabled="publishing || !content.trim()"
            class="w-full py-2.5 rounded-xl bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan text-white text-xs font-bold shadow-lg shadow-amber-500/20 hover:opacity-95 disabled:opacity-50 transition-all flex items-center justify-center space-x-1.5"
          >
            <Loader2 v-if="publishing" class="w-3.5 h-3.5 animate-spin" />
            <Sparkles v-else class="w-3.5 h-3.5" />
            <span>{{ publishing ? '广播发射中...' : '发射星际碎语 🚀' }}</span>
          </button>
        </form>
      </div>

      <!-- 右侧：最近碎语时间线预览 -->
      <div class="md:col-span-2 glass-card p-6 rounded-2xl border border-white/10 space-y-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-2">
            <MessageSquare class="w-4 h-4 text-amber-400" />
            <h3 class="text-sm font-bold text-white">碎语时间线广播记录 ({{ moments.length }})</h3>
          </div>
          <button
            @click="fetchMoments"
            :disabled="loading"
            class="p-1.5 rounded-lg border border-white/10 hover:border-white/20 bg-white/5 text-slate-300 hover:text-white transition-colors"
            title="刷新"
          >
            <RefreshCw class="w-3.5 h-3.5" :class="{ 'animate-spin': loading }" />
          </button>
        </div>

        <div v-if="loading" class="py-12 text-center text-slate-400 flex flex-col items-center justify-center space-y-2">
          <Loader2 class="w-5 h-5 animate-spin text-amber-400" />
          <span class="text-xs font-mono">接收碎语广播中...</span>
        </div>

        <div v-else-if="moments.length === 0" class="py-12 text-center text-slate-500 text-xs">
          暂无碎语，请在左侧发射第一条说说
        </div>

        <div v-else class="space-y-3 max-h-[550px] overflow-y-auto pr-1">
          <div
            v-for="item in moments"
            :key="item.id"
            class="p-4 rounded-xl bg-space-950/70 border border-white/10 space-y-2 hover:border-white/20 transition-all"
          >
            <div class="flex items-center justify-between text-xs">
              <div class="flex items-center space-x-2">
                <span class="text-base">{{ item.moodEmoji || '✨' }}</span>
                <span v-if="item.location" class="text-slate-400 font-mono text-[11px] flex items-center space-x-1">
                  <MapPin class="w-3 h-3 text-nebula-cyan" />
                  <span>{{ item.location }}</span>
                </span>
              </div>
              <span class="text-[10px] text-slate-500 font-mono">{{ formatDate(item.createdAt) }}</span>
            </div>
            <p class="text-xs text-slate-200 leading-relaxed whitespace-pre-wrap">
              {{ item.content }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import {
  Send,
  MapPin,
  MessageSquare,
  RefreshCw,
  Sparkles,
  Loader2
} from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { publishMoment } from '@/api/admin';
import { getMoments } from '@/api/moments';
import type { Moment } from '@/types';

const moments = ref<Moment[]>([]);
const loading = ref(false);
const publishing = ref(false);

const selectedEmoji = ref('🚀');
const locationInput = ref('星穹列车 · 观景车厢');
const content = ref('');

const moodOptions = [
  { emoji: '🚀', label: '启航' },
  { emoji: '☕', label: '摸鱼' },
  { emoji: '🌌', label: '漫游' },
  { emoji: '🎮', label: '星铁' },
  { emoji: '💡', label: '灵感' },
  { emoji: '🐾', label: '帕姆' },
  { emoji: '✨', label: '闪耀' },
  { emoji: '💤', label: '休眠' },
];

const presetLocations = [
  '星穹列车 · 观景车厢',
  '黑塔空间站 · 主控舱段',
  '香港城市大学（东莞）',
  '复旦大学 · 张江校区',
  '数字星海 · 赛博自留地',
];

const fetchMoments = async () => {
  loading.value = true;
  try {
    const list = await getMoments();
    moments.value = list || [];
  } catch (err: any) {
    console.error('获取碎语失败:', err);
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateStr?: string) => {
  if (!dateStr) return '-';
  return dateStr.replace('T', ' ').slice(0, 16);
};

const handlePublish = async () => {
  if (!content.value.trim()) return;

  publishing.value = true;
  try {
    const newMoment = await publishMoment({
      content: content.value.trim(),
      mood: selectedEmoji.value,
      location: locationInput.value.trim() || undefined,
    } as any);

    moments.value.unshift(newMoment);
    content.value = '';

    confetti({
      particleCount: 40,
      spread: 50,
      origin: { y: 0.6 },
      colors: ['#38bdf8', '#f59e0b', '#ec4899', '#10b981'],
    });

    alert('星际碎语发射成功 ✨');
  } catch (err: any) {
    alert(err.message || '碎语发射失败');
  } finally {
    publishing.value = false;
  }
};

onMounted(() => {
  fetchMoments();
});
</script>
