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
    <div class="glass-card rounded-2xl p-5 border border-white/10 space-y-4 shadow-xl">
      <!-- 文本输入框 -->
      <textarea
        v-model="newContent"
        rows="3"
        placeholder="这一刻在想什么？发送一条星际电波吧..."
        class="w-full bg-space-950/60 border border-white/10 rounded-xl p-3.5 text-sm text-slate-200 placeholder-slate-500 focus:outline-none focus:border-nebula-purple/50 transition-colors resize-none leading-relaxed"
      ></textarea>

      <!-- 待发送图片缩略图排盘 -->
      <div v-if="selectedImages.length > 0" class="flex flex-wrap gap-2.5 pt-1">
        <div
          v-for="(img, idx) in selectedImages"
          :key="idx"
          class="w-16 h-16 sm:w-20 sm:h-20 rounded-xl border border-white/20 overflow-hidden relative group shrink-0 shadow-md bg-space-900"
        >
          <img :src="img" class="w-full h-full object-cover" />
          <button
            type="button"
            @click="removeImage(idx)"
            class="absolute inset-0 bg-black/60 opacity-0 group-hover:opacity-100 flex items-center justify-center text-rose-400 hover:text-rose-300 transition-opacity"
            title="移除此图"
          >
            <X class="w-5 h-5" />
          </button>
        </div>

        <!-- 继续添加图片加号卡片 (未达9张) -->
        <button
          v-if="selectedImages.length < 9"
          type="button"
          @click="triggerFileInput"
          class="w-16 h-16 sm:w-20 sm:h-20 rounded-xl border-2 border-dashed border-white/20 hover:border-nebula-purple/60 text-slate-400 hover:text-white flex flex-col items-center justify-center transition-all bg-white/5 active:scale-95"
          title="继续添加图片"
        >
          <Plus class="w-5 h-5" />
          <span class="text-[10px] font-mono mt-0.5">{{ selectedImages.length }}/9</span>
        </button>
      </div>

      <!-- 隐藏的原生文件输入框 -->
      <input
        ref="fileInputRef"
        type="file"
        multiple
        accept="image/*"
        class="hidden"
        @change="handleFileSelect"
      />

      <!-- 网络图片直链与快捷预设折叠栏 -->
      <transition
        enter-active-class="transition duration-200 ease-out"
        enter-from-class="opacity-0 -translate-y-2"
        enter-to-class="opacity-100 translate-y-0"
        leave-active-class="transition duration-150 ease-in"
        leave-from-class="opacity-100 translate-y-0"
        leave-to-class="opacity-0 -translate-y-2"
      >
        <div v-if="showUrlInput" class="p-3 rounded-xl bg-space-950/80 border border-white/10 space-y-2.5">
          <div class="flex items-center space-x-2">
            <input
              v-model="imageUrlInput"
              type="text"
              placeholder="输入图片直链 URL (例如 https://...)"
              class="flex-1 px-3 py-1.5 rounded-lg bg-white/5 border border-white/10 text-xs text-slate-200 placeholder-slate-500 focus:outline-none focus:border-nebula-purple font-mono"
              @keyup.enter="addImageUrl"
            />
            <button
              type="button"
              @click="addImageUrl"
              class="px-3.5 py-1.5 rounded-lg bg-nebula-purple/20 hover:bg-nebula-purple/40 border border-nebula-purple/40 text-xs text-purple-200 font-medium transition-colors"
            >
              添加
            </button>
          </div>

          <!-- 星际快捷预设壁纸/配图点选 -->
          <div class="flex flex-wrap items-center gap-1.5 pt-0.5 text-xs text-slate-400">
            <span class="text-[10px] text-slate-500 font-mono">快捷配图:</span>
            <button
              v-for="preset in presetImages"
              :key="preset.url"
              type="button"
              @click="addPresetImage(preset.url)"
              class="px-2.5 py-1 rounded-lg bg-white/5 hover:bg-white/10 hover:text-nebula-cyan border border-white/5 transition-colors text-[11px]"
            >
              + {{ preset.label }}
            </button>
          </div>
        </div>
      </transition>

      <!-- 底部控制条：心情、配图入口、地点、发射 -->
      <div class="flex flex-wrap items-center justify-between gap-3 pt-1 border-t border-white/5">
        <div class="flex flex-wrap items-center gap-2.5">
          <!-- 心情 Emoji 快速选择 -->
          <div class="flex items-center space-x-1 bg-white/5 rounded-lg p-1 border border-white/5">
            <button
              v-for="emoji in moodOptions"
              :key="emoji"
              @click="selectedMood = emoji"
              class="w-7 h-7 rounded flex items-center justify-center text-sm transition-all"
              :class="selectedMood === emoji ? 'bg-nebula-purple/30 scale-110 shadow-sm' : 'hover:bg-white/10 opacity-70 hover:opacity-100'"
              title="选择当前心情"
            >
              {{ emoji }}
            </button>
          </div>

          <!-- 图片添加按钮组 -->
          <div class="flex items-center space-x-1">
            <button
              type="button"
              @click="triggerFileInput"
              :disabled="selectedImages.length >= 9"
              class="flex items-center space-x-1 text-xs text-slate-300 bg-white/5 hover:bg-white/10 px-2.5 py-1.5 rounded-lg border border-white/10 hover:border-nebula-purple/40 transition-all disabled:opacity-40"
              title="添加本地图片 (最多9张)"
            >
              <ImageIcon class="w-3.5 h-3.5 text-nebula-cyan" />
              <span>图片</span>
              <span v-if="selectedImages.length > 0" class="text-[10px] font-mono text-nebula-cyan font-bold">
                ({{ selectedImages.length }}/9)
              </span>
            </button>

            <button
              type="button"
              @click="showUrlInput = !showUrlInput"
              class="p-1.5 rounded-lg bg-white/5 hover:bg-white/10 text-slate-400 hover:text-nebula-cyan border border-white/10 transition-colors"
              :class="{ 'text-nebula-cyan border-nebula-cyan/30': showUrlInput }"
              title="网络外链 / 预设配图"
            >
              <Link2 class="w-3.5 h-3.5" />
            </button>
          </div>

          <!-- 地点 -->
          <div class="flex items-center space-x-1 text-xs text-slate-400 bg-white/5 px-2.5 py-1.5 rounded-lg border border-white/5">
            <MapPin class="w-3.5 h-3.5 text-nebula-pink" />
            <input
              v-model="customLocation"
              type="text"
              class="bg-transparent border-none outline-none text-xs text-slate-300 w-24 placeholder-slate-500"
              placeholder="地点坐标"
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
          {{ moment.moodEmoji || moment.mood || '✨' }}
        </div>

        <!-- 碎语卡片内容 -->
        <div class="flex-1 glass-card rounded-2xl p-5 border border-white/10 space-y-3 group-hover:border-nebula-purple/30 transition-all shadow-md">
          <div class="flex items-center justify-between text-xs text-slate-500 font-mono">
            <span v-if="moment.location" class="flex items-center space-x-1 text-slate-400">
              <MapPin class="w-3 h-3 text-nebula-pink" />
              <span>{{ moment.location }}</span>
            </span>
            <span v-else>星向电台</span>
            <span>{{ formatTime(moment.createdAt) }}</span>
          </div>

          <!-- 正文 -->
          <p class="text-sm text-slate-200 leading-relaxed whitespace-pre-wrap">
            {{ moment.content }}
          </p>

          <!-- 朋友圈经典自适应九宫格图片展示 -->
          <div v-if="getMomentImages(moment).length > 0" class="pt-1">
            <!-- 单图大图展示 -->
            <div
              v-if="getMomentImages(moment).length === 1"
              class="max-w-md max-h-80 rounded-2xl overflow-hidden border border-white/10 shadow-lg cursor-pointer group/img relative"
              @click="openLightbox(getMomentImages(moment), 0)"
            >
              <img
                :src="getMomentImages(moment)[0]"
                alt="Moment photo"
                class="w-full h-full object-cover transition-transform duration-300 group-hover/img:scale-105"
                loading="lazy"
              />
            </div>

            <!-- 2 或 4 张图：双列对称网格 -->
            <div
              v-else-if="getMomentImages(moment).length === 2 || getMomentImages(moment).length === 4"
              class="grid grid-cols-2 gap-2 max-w-sm"
            >
              <div
                v-for="(img, imgIdx) in getMomentImages(moment)"
                :key="imgIdx"
                class="aspect-square rounded-xl overflow-hidden border border-white/10 shadow cursor-pointer group/img relative bg-space-900"
                @click="openLightbox(getMomentImages(moment), imgIdx)"
              >
                <img
                  :src="img"
                  alt="Moment photo"
                  class="w-full h-full object-cover transition-transform duration-300 group-hover/img:scale-110"
                  loading="lazy"
                />
              </div>
            </div>

            <!-- 3、5~9 张图：经典三列九宫格 -->
            <div
              v-else
              class="grid grid-cols-3 gap-2 max-w-md"
            >
              <div
                v-for="(img, imgIdx) in getMomentImages(moment)"
                :key="imgIdx"
                class="aspect-square rounded-xl overflow-hidden border border-white/10 shadow cursor-pointer group/img relative bg-space-900"
                @click="openLightbox(getMomentImages(moment), imgIdx)"
              >
                <img
                  :src="img"
                  alt="Moment photo"
                  class="w-full h-full object-cover transition-transform duration-300 group-hover/img:scale-110"
                  loading="lazy"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 全屏图片查看器 Lightbox (Teleport to body 彻底破除层叠限制) -->
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
  Sparkles,
  MapPin,
  Send,
  Image as ImageIcon,
  Link2,
  X,
  Plus,
  ChevronLeft,
  ChevronRight
} from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { getMoments, createMoment } from '@/api/moments';
import type { Moment } from '@/types';

const moments = ref<Moment[]>([]);
const loading = ref(true);
const publishing = ref(false);
const postError = ref('');

const newContent = ref('');
const selectedMood = ref('🚀');
const customLocation = ref('星穹列车 · 观景车厢');
const moodOptions = ['🚀', '🌟', '☕️', '🪐', '💻', '🌸'];

// 配图相关响应式变量
const selectedImages = ref<string[]>([]);
const showUrlInput = ref(false);
const imageUrlInput = ref('');
const fileInputRef = ref<HTMLInputElement | null>(null);

const presetImages = [
  { label: '三月七自拍', url: '/images/hsr/march7th_selfie.png' },
  { label: '姬子与列车', url: '/images/hsr/himeko_express.png' },
  { label: '丹恒', url: '/images/hsr/danheng.png' },
  { label: '瓦尔特', url: '/images/hsr/welt.png' },
  { label: '帕姆列车长', url: '/images/pompom.png' },
];

// 大图全屏查看 Lightbox
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

// 触发本地文件选择
const triggerFileInput = () => {
  if (fileInputRef.value) {
    fileInputRef.value.click();
  }
};

// 客户端图片轻量级压缩为 WebP/JPEG DataURL
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

// 本地文件选取回调
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

// 移除已选图片
const removeImage = (index: number) => {
  selectedImages.value.splice(index, 1);
};

// 添加网络外链
const addImageUrl = () => {
  const url = imageUrlInput.value.trim();
  if (!url) return;
  if (selectedImages.value.length < 9) {
    selectedImages.value.push(url);
    imageUrlInput.value = '';
  }
};

// 添加预设壁纸/配图
const addPresetImage = (url: string) => {
  if (selectedImages.value.length < 9 && !selectedImages.value.includes(url)) {
    selectedImages.value.push(url);
  }
};

// 解析碎语数据中的图片列表
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
      location: customLocation.value.trim() || undefined,
      imagesJson: selectedImages.value.length ? JSON.stringify(selectedImages.value) : undefined
    });

    // 燃放星轨礼花
    confetti({
      particleCount: 50,
      spread: 60,
      origin: { y: 0.8 },
      colors: ['#38bdf8', '#a855f7', '#ec4899']
    });

    newContent.value = '';
    selectedImages.value = [];
    showUrlInput.value = false;
    imageUrlInput.value = '';

    await loadMoments();
  } catch (err: any) {
    postError.value = err.response?.data?.message || err.message || '发布失败';
  } finally {
    publishing.value = false;
  }
};

onMounted(() => {
  loadMoments();
  window.addEventListener('keydown', handleKeyDown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleKeyDown);
});
</script>
