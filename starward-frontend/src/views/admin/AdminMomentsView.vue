<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 items-start">
      <!-- 左侧：发射台卡片 -->
      <div class="glass-card p-6 rounded-2xl border border-white/10 space-y-4 shadow-xl">
        <div class="flex items-center space-x-2">
          <div class="p-2 rounded-xl bg-nebula-cyan/10 text-nebula-cyan border border-nebula-cyan/20">
            <Send class="w-4 h-4" />
          </div>
          <div>
            <h3 class="text-sm font-bold text-white">星际碎语速发发射台</h3>
            <p class="text-[11px] text-slate-400">一键将生活随笔、配图与灵感广播至展示端</p>
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
                :class="selectedEmoji === item.emoji ? 'bg-amber-500/20 border-amber-400 text-amber-700 dark:text-white font-bold shadow' : 'bg-white/80 dark:bg-space-950/80 border-pink-200/70 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
              >
                <span>{{ item.emoji }}</span>
                <span class="text-[10px]">{{ item.label }}</span>
              </button>
            </div>
          </div>

          <!-- 地点 / 空间坐标 -->
          <div>
            <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
              空间坐标 (Location)
            </label>
            <div class="relative">
              <MapPin class="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-slate-400 dark:text-slate-500" />
              <input
                v-model="locationInput"
                type="text"
                placeholder="例如：星穹列车 · 观景车厢"
                class="w-full pl-8 pr-3 py-2 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all font-mono"
              />
            </div>
            <!-- 预设地点 -->
            <div class="flex flex-wrap gap-1.5 mt-1.5">
              <button
                v-for="loc in presetLocations"
                :key="loc"
                type="button"
                @click="locationInput = loc"
                class="text-[10px] text-slate-500 hover:text-pink-600 dark:hover:text-nebula-cyan transition-colors"
              >
                #{{ loc }}
              </button>
            </div>
          </div>

          <!-- 正文 -->
          <div>
            <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
              碎语正文 <span class="text-rose-500">*</span>
            </label>
            <textarea
              v-model="content"
              rows="3"
              placeholder="分享此时此刻的技术突破、列车航行记录或摸鱼瞬间..."
              class="w-full px-3.5 py-2.5 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all leading-relaxed resize-none font-mono"
            ></textarea>
          </div>

          <!-- 待发射配图预览 -->
          <div v-if="selectedImages.length > 0" class="space-y-1.5">
            <div class="flex items-center justify-between text-[11px] text-slate-500 dark:text-slate-400 font-mono">
              <span>已选配图 ({{ selectedImages.length }}/9)</span>
              <button type="button" @click="selectedImages = []" class="text-rose-500 hover:underline">清空</button>
            </div>
            <div class="flex flex-wrap gap-2">
              <div
                v-for="(img, idx) in selectedImages"
                :key="idx"
                class="w-14 h-14 rounded-lg border border-pink-200/50 dark:border-white/15 overflow-hidden relative group shrink-0 bg-pink-100 dark:bg-space-900 shadow"
              >
                <img :src="img" class="w-full h-full object-cover" />
                <button
                  type="button"
                  @click="removeImage(idx)"
                  class="absolute inset-0 bg-black/60 opacity-0 group-hover:opacity-100 flex items-center justify-center text-rose-400 hover:text-rose-300 transition-opacity"
                  title="删除"
                >
                  <X class="w-4 h-4" />
                </button>
              </div>
            </div>
          </div>

          <!-- 图片操作栏 -->
          <div class="space-y-2">
            <input
              ref="fileInputRef"
              type="file"
              multiple
              accept="image/*"
              class="hidden"
              @change="handleFileSelect"
            />

            <div class="flex items-center space-x-2">
              <button
                type="button"
                @click="triggerFileInput"
                :disabled="selectedImages.length >= 9"
                class="flex-1 py-1.5 px-2.5 rounded-xl bg-white/5 hover:bg-white/10 border border-white/10 hover:border-amber-400/40 text-xs text-slate-300 flex items-center justify-center space-x-1.5 transition-all disabled:opacity-40"
              >
                <ImageIcon class="w-3.5 h-3.5 text-amber-400" />
                <span>本地选图 ({{ selectedImages.length }}/9)</span>
              </button>

              <button
                type="button"
                @click="showUrlInput = !showUrlInput"
                class="p-2 rounded-xl bg-white/5 hover:bg-white/10 border border-white/10 text-slate-400 hover:text-amber-400 transition-colors"
                :class="{ 'text-amber-400 border-amber-400/40': showUrlInput }"
                title="外链与星轨预设"
              >
                <Link2 class="w-3.5 h-3.5" />
              </button>
            </div>

            <!-- 外链与预设输入框 -->
            <div v-if="showUrlInput" class="p-2.5 rounded-xl bg-pink-100/70 dark:bg-space-950 border border-pink-200/70 dark:border-white/10 space-y-2">
              <div class="flex items-center space-x-1.5">
                <input
                  v-model="imageUrlInput"
                  type="text"
                  placeholder="图片 URL (https://...)"
                  class="flex-1 px-2.5 py-1 rounded-lg bg-white/80 dark:bg-white/5 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-200 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 font-mono"
                  @keyup.enter="addImageUrl"
                />
                <button
                  type="button"
                  @click="addImageUrl"
                  class="px-2.5 py-1 rounded-lg bg-amber-400/20 text-amber-700 dark:text-amber-300 text-xs font-semibold hover:bg-amber-400/30"
                >
                  添加
                </button>
              </div>
              <div class="flex flex-wrap gap-1 text-[10px]">
                <span class="text-slate-500 font-mono">预设:</span>
                <button
                  v-for="p in presetImages"
                  :key="p.url"
                  type="button"
                  @click="addPresetImage(p.url)"
                  class="px-1.5 py-0.5 rounded bg-white/60 dark:bg-white/5 hover:bg-pink-100 dark:hover:bg-white/10 text-slate-600 dark:text-slate-400 hover:text-amber-600 dark:hover:text-amber-400"
                >
                  +{{ p.label }}
                </button>
              </div>
            </div>
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
      <div class="md:col-span-2 glass-card p-6 rounded-2xl border border-pink-200/50 dark:border-white/10 space-y-4 shadow-xl">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-2">
            <MessageSquare class="w-4 h-4 text-amber-500 dark:text-amber-400" />
            <h3 class="text-sm font-bold text-slate-800 dark:text-white">碎语时间线广播记录 ({{ moments.length }})</h3>
          </div>
          <button
            @click="fetchMoments"
            :disabled="loading"
            class="p-1.5 rounded-lg border border-pink-200/60 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 bg-white/60 dark:bg-white/5 text-slate-600 dark:text-slate-300 hover:text-slate-900 dark:hover:text-white transition-colors"
            title="刷新"
          >
            <RefreshCw class="w-3.5 h-3.5" :class="{ 'animate-spin': loading }" />
          </button>
        </div>

        <div v-if="loading" class="py-12 text-center text-slate-500 dark:text-slate-400 flex flex-col items-center justify-center space-y-2">
          <Loader2 class="w-5 h-5 animate-spin text-amber-400" />
          <span class="text-xs font-mono">接收碎语广播中...</span>
        </div>

        <div v-else-if="moments.length === 0" class="py-12 text-center text-slate-400 dark:text-slate-500 text-xs">
          暂无碎语，请在左侧发射第一条说说
        </div>

        <div v-else class="space-y-3 max-h-[620px] overflow-y-auto pr-1">
          <div
            v-for="item in moments"
            :key="item.id"
            class="p-4 rounded-xl bg-white/70 dark:bg-space-950/70 border border-pink-200/50 dark:border-white/10 space-y-2.5 hover:border-pink-300 dark:hover:border-white/20 transition-all shadow-sm"
          >
            <div class="flex items-center justify-between text-xs">
              <div class="flex items-center space-x-2">
                <span class="text-base">{{ item.moodEmoji || item.mood || '✨' }}</span>
                <span v-if="item.location" class="text-slate-500 dark:text-slate-400 font-mono text-[11px] flex items-center space-x-1">
                  <MapPin class="w-3 h-3 text-pink-600 dark:text-nebula-cyan" />
                  <span>{{ item.location }}</span>
                </span>
              </div>
              <span class="text-[10px] text-slate-500 font-mono">{{ formatDate(item.createdAt) }}</span>
            </div>

            <!-- 正文 -->
            <p class="text-xs text-slate-700 dark:text-slate-200 leading-relaxed whitespace-pre-wrap">
              {{ item.content }}
            </p>

            <!-- 附带配图网格 -->
            <div v-if="getMomentImages(item).length > 0" class="pt-1">
              <!-- 单图 -->
              <div
                v-if="getMomentImages(item).length === 1"
                class="max-w-xs max-h-48 rounded-xl overflow-hidden border border-white/10 cursor-pointer group/img"
                @click="openLightbox(getMomentImages(item), 0)"
              >
                <img :src="getMomentImages(item)[0]" class="w-full h-full object-cover group-hover/img:scale-105 transition-transform" />
              </div>

              <!-- 多图 -->
              <div
                v-else
                class="grid gap-1.5 max-w-xs"
                :class="getMomentImages(item).length === 2 || getMomentImages(item).length === 4 ? 'grid-cols-2' : 'grid-cols-3'"
              >
                <div
                  v-for="(img, imgIdx) in getMomentImages(item)"
                  :key="imgIdx"
                  class="aspect-square rounded-lg overflow-hidden border border-white/10 cursor-pointer group/img bg-space-900"
                  @click="openLightbox(getMomentImages(item), imgIdx)"
                >
                  <img :src="img" class="w-full h-full object-cover group-hover/img:scale-110 transition-transform" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 全屏图片查看器 Lightbox (Teleport to body) -->
    <teleport to="body">
      <transition
        enter-active-class="transition duration-200 ease-out"
        enter-from-class="opacity-0 scale-95"
        enter-to-class="opacity-100 scale-100"
        leave-active-class="transition duration-150 ease-in"
        leave-from-class="opacity-100 scale-100"
        leave-to-class="opacity-0 scale-95"
      >
        <div
          v-if="lightboxOpen"
          class="fixed inset-0 z-[100] bg-black/90 backdrop-blur-xl flex items-center justify-center select-none"
          @click="closeLightbox"
        >
          <!-- 顶栏操作区 -->
          <div class="absolute top-5 inset-x-5 flex items-center justify-between z-10" @click.stop>
            <div class="px-3.5 py-1 rounded-full bg-white/10 backdrop-blur-md text-xs font-mono text-slate-300 border border-white/10">
              {{ activeLightboxIndex + 1 }} / {{ lightboxImages.length }}
            </div>
            <button
              @click="closeLightbox"
              class="p-2 rounded-full bg-white/10 hover:bg-white/20 text-white transition-colors border border-white/10"
              title="关闭 (ESC)"
            >
              <X class="w-5 h-5" />
            </button>
          </div>

          <!-- 左右切换按键 -->
          <button
            v-if="lightboxImages.length > 1"
            @click.stop="prevLightboxImage"
            class="absolute left-4 top-1/2 -translate-y-1/2 p-3 rounded-full bg-white/10 hover:bg-white/20 text-white transition-all border border-white/10 shadow-xl"
            title="上一张 (←)"
          >
            <ChevronLeft class="w-6 h-6" />
          </button>

          <button
            v-if="lightboxImages.length > 1"
            @click.stop="nextLightboxImage"
            class="absolute right-4 top-1/2 -translate-y-1/2 p-3 rounded-full bg-white/10 hover:bg-white/20 text-white transition-all border border-white/10 shadow-xl"
            title="下一张 (→)"
          >
            <ChevronRight class="w-6 h-6" />
          </button>

          <!-- 核心图片展示容器 -->
          <div class="max-w-[90vw] max-h-[85vh] p-2" @click.stop>
            <img
              :src="lightboxImages[activeLightboxIndex]"
              alt="Full view"
              class="max-w-full max-h-[82vh] object-contain rounded-2xl shadow-2xl mx-auto border border-white/10"
            />
          </div>
        </div>
      </transition>
    </teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';
import {
  Send,
  MapPin,
  MessageSquare,
  RefreshCw,
  Sparkles,
  Loader2,
  Image as ImageIcon,
  Link2,
  X,
  ChevronLeft,
  ChevronRight
} from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { publishMoment } from '@/api/admin';
import { getMoments } from '@/api/moments';
import type { Moment } from '@/types';
import { useToast } from '@/composables/useToast';

const toast = useToast();
const moments = ref<Moment[]>([]);
const loading = ref(false);
const publishing = ref(false);

const selectedEmoji = ref('🚀');
const locationInput = ref('星穹列车 · 观景车厢');
const content = ref('');

// 配图管理
const selectedImages = ref<string[]>([]);
const showUrlInput = ref(false);
const imageUrlInput = ref('');
const fileInputRef = ref<HTMLInputElement | null>(null);

const presetImages = [
  { label: '三月七自拍', url: '/images/hsr/march7th_selfie.png' },
  { label: '姬子与列车', url: '/images/hsr/himeko_express.png' },
  { label: '帕姆列车长', url: '/images/pompom.png' },
  { label: '丹恒', url: '/images/hsr/danheng.png' },
];

// 大图 Lightbox
const lightboxOpen = ref(false);
const lightboxImages = ref<string[]>([]);
const activeLightboxIndex = ref(0);

const openLightbox = (imgs: string[], index = 0) => {
  if (!imgs || !imgs.length) return;
  lightboxImages.value = imgs;
  activeLightboxIndex.value = Math.max(0, Math.min(index, imgs.length - 1));
  lightboxOpen.value = true;
};

const closeLightbox = () => {
  lightboxOpen.value = false;
};

const prevLightboxImage = () => {
  if (activeLightboxIndex.value > 0) {
    activeLightboxIndex.value--;
  } else {
    activeLightboxIndex.value = lightboxImages.value.length - 1;
  }
};

const nextLightboxImage = () => {
  if (activeLightboxIndex.value < lightboxImages.value.length - 1) {
    activeLightboxIndex.value++;
  } else {
    activeLightboxIndex.value = 0;
  }
};

const handleKeyDown = (e: KeyboardEvent) => {
  if (!lightboxOpen.value) return;
  if (e.key === 'Escape') closeLightbox();
  if (e.key === 'ArrowLeft') prevLightboxImage();
  if (e.key === 'ArrowRight') nextLightboxImage();
};

const triggerFileInput = () => {
  if (fileInputRef.value) {
    fileInputRef.value.click();
  }
};

// 客户端压缩
const compressImage = (file: File, maxWidth = 1200, quality = 0.82): Promise<string> => {
  return new Promise((resolve) => {
    const reader = new FileReader();
    reader.onload = (e) => {
      const img = new window.Image();
      img.onload = () => {
        let width = img.width;
        let height = img.height;
        if (width > maxWidth) {
          height = Math.round((height * maxWidth) / width);
          width = maxWidth;
        }
        const canvas = document.createElement('canvas');
        canvas.width = width;
        canvas.height = height;
        const ctx = canvas.getContext('2d');
        if (ctx) {
          ctx.drawImage(img, 0, 0, width, height);
          resolve(canvas.toDataURL('image/jpeg', quality));
        } else {
          resolve(e.target?.result as string);
        }
      };
      img.onerror = () => resolve(e.target?.result as string);
      img.src = e.target?.result as string;
    };
    reader.readAsDataURL(file);
  });
};

const handleFileSelect = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const files = target.files;
  if (!files || !files.length) return;

  for (let i = 0; i < files.length; i++) {
    if (selectedImages.value.length >= 9) break;
    const compressed = await compressImage(files[i]);
    selectedImages.value.push(compressed);
  }
  target.value = '';
};

const removeImage = (index: number) => {
  selectedImages.value.splice(index, 1);
};

const addImageUrl = () => {
  const url = imageUrlInput.value.trim();
  if (!url) return;
  if (selectedImages.value.length < 9) {
    selectedImages.value.push(url);
    imageUrlInput.value = '';
  }
};

const addPresetImage = (url: string) => {
  if (selectedImages.value.length < 9 && !selectedImages.value.includes(url)) {
    selectedImages.value.push(url);
  }
};

const getMomentImages = (moment: Moment): string[] => {
  if (!moment.imagesJson) return [];
  try {
    const parsed = JSON.parse(moment.imagesJson);
    if (Array.isArray(parsed)) return parsed.filter(Boolean);
    if (typeof parsed === 'string' && parsed) return [parsed];
    return [];
  } catch {
    if (
      moment.imagesJson.startsWith('http') ||
      moment.imagesJson.startsWith('/') ||
      moment.imagesJson.startsWith('data:image')
    ) {
      return [moment.imagesJson];
    }
    return [];
  }
};

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
      imagesJson: selectedImages.value.length ? JSON.stringify(selectedImages.value) : undefined
    });

    moments.value.unshift(newMoment);
    content.value = '';
    selectedImages.value = [];
    showUrlInput.value = false;
    imageUrlInput.value = '';

    confetti({
      particleCount: 40,
      spread: 50,
      origin: { y: 0.6 },
      colors: ['#38bdf8', '#f59e0b', '#ec4899', '#10b981'],
    });

    toast.success('星际碎语发射成功 ✨');
  } catch (err: any) {
    toast.error(err.response?.data?.message || err.message || '碎语发射失败');
  } finally {
    publishing.value = false;
  }
};

onMounted(() => {
  fetchMoments();
  window.addEventListener('keydown', handleKeyDown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyDown);
});
</script>
