<template>
  <div class="max-w-3xl mx-auto px-4 sm:px-6 py-10 space-y-8">
    <!-- 头部说明区 -->
    <div class="space-y-3 border-b border-pink-200/60 dark:border-white/10 pb-6">
      <h1 class="text-3xl font-extrabold text-slate-800 dark:text-white tracking-tight flex items-center space-x-3">
        <Sparkles class="w-8 h-8 text-pink-500 dark:text-nebula-purple" />
        <span>星际碎语 · Moments</span>
      </h1>
      <p class="text-sm text-slate-600 dark:text-slate-400">
        记录瞬间的灵感、深夜敲代码的碎碎念、以及生活的细碎光芒。
      </p>
    </div>

    <!-- 碎语发布面板 (与控制中枢发射台全面对齐) -->
    <div class="glass-card rounded-2xl p-6 border border-pink-200/60 dark:border-white/10 space-y-4 shadow-xl">
      <div class="flex items-center justify-between pb-1 border-b border-pink-200/40 dark:border-white/5">
        <div class="flex items-center space-x-2">
          <div class="p-2 rounded-xl bg-pink-500/10 text-pink-600 dark:bg-nebula-cyan/10 dark:text-nebula-cyan border border-pink-200/60 dark:border-nebula-cyan/20">
            <Send class="w-4 h-4" />
          </div>
          <div>
            <h3 class="text-sm font-bold text-slate-800 dark:text-white">星际碎语速发发射台</h3>
            <p class="text-[11px] text-slate-500 dark:text-slate-400">一键将生活随笔、配图与灵感广播至展示端</p>
          </div>
        </div>
        <div>
          <span
            v-if="isAdmin"
            class="px-2.5 py-1 rounded-full text-[10px] font-medium bg-emerald-500/15 text-emerald-700 dark:text-emerald-300 border border-emerald-500/30 flex items-center space-x-1"
          >
            <span class="w-1.5 h-1.5 rounded-full bg-emerald-500 animate-pulse"></span>
            <span>列车长在线</span>
          </span>
          <router-link
            v-else
            to="/admin/login"
            class="px-2.5 py-1 rounded-full text-[10px] font-medium bg-pink-100 text-pink-700 dark:bg-white/10 dark:text-slate-300 border border-pink-200 dark:border-white/10 hover:border-pink-400 transition-colors flex items-center space-x-1"
            title="点击前往控制中枢登录"
          >
            <span>🔒 访客模式 · 登录</span>
          </router-link>
        </div>
      </div>

      <form @submit.prevent="handlePublish" class="space-y-4">
        <!-- 心情 Emoji 快速点选 -->
        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1.5 flex items-center justify-between">
            <span>当刻星际心境</span>
            <span class="text-base">{{ selectedMood }}</span>
          </label>
          <div class="flex flex-wrap gap-2">
            <button
              v-for="item in moodOptions"
              :key="item.emoji"
              type="button"
              @click="selectedMood = item.emoji"
              class="px-2.5 py-1 rounded-xl text-xs border transition-all flex items-center space-x-1"
              :class="selectedMood === item.emoji ? 'bg-amber-500/20 border-amber-400 text-amber-700 dark:text-white font-bold shadow' : 'bg-white/80 dark:bg-space-950/80 border-pink-200/70 dark:border-white/10 text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
            >
              <span>{{ item.emoji }}</span>
              <span class="text-[10px]">{{ item.label }}</span>
            </button>
          </div>
        </div>

        <!-- 空间坐标 (Location) -->
        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
            空间坐标 (Location)
          </label>
          <div class="relative">
            <MapPin class="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-slate-400 dark:text-slate-500" />
            <input
              v-model="customLocation"
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
              @click="customLocation = loc"
              class="text-[10px] text-slate-500 hover:text-pink-600 dark:hover:text-nebula-cyan transition-colors"
            >
              #{{ loc }}
            </button>
          </div>
        </div>

        <!-- 碎语正文 -->
        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
            碎语正文 <span class="text-rose-500">*</span>
          </label>
          <textarea
            v-model="newContent"
            rows="3"
            placeholder="分享此时此刻的技术突破、列车航行记录或摸鱼瞬间..."
            class="w-full px-3.5 py-2.5 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all leading-relaxed resize-none font-mono"
          ></textarea>
        </div>

        <!-- 待发送图片缩略图排盘 -->
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
          <!-- 隐藏的原生文件输入框 -->
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
              class="flex-1 py-1.5 px-2.5 rounded-xl bg-white/70 dark:bg-white/5 hover:bg-pink-100/60 dark:hover:bg-white/10 border border-pink-200/70 dark:border-white/10 hover:border-pink-400 dark:hover:border-amber-400/40 text-xs text-slate-700 dark:text-slate-300 flex items-center justify-center space-x-1.5 transition-all disabled:opacity-40"
            >
              <ImageIcon class="w-3.5 h-3.5 text-pink-500 dark:text-amber-400" />
              <span>本地选图 ({{ selectedImages.length }}/9)</span>
            </button>

            <button
              type="button"
              @click="showUrlInput = !showUrlInput"
              class="p-2 rounded-xl bg-white/70 dark:bg-white/5 hover:bg-pink-100/60 dark:hover:bg-white/10 border border-pink-200/70 dark:border-white/10 text-slate-500 dark:text-slate-400 hover:text-pink-600 dark:hover:text-amber-400 transition-colors"
              :class="{ 'text-pink-600 border-pink-400 dark:text-amber-400 dark:border-amber-400/40': showUrlInput }"
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
                class="flex-1 px-2.5 py-1 rounded-lg bg-white/80 dark:bg-white/5 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-200 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-pink-500 font-mono"
                @keyup.enter="addImageUrl"
              />
              <button
                type="button"
                @click="addImageUrl"
                class="px-2.5 py-1 rounded-lg bg-pink-500/20 text-pink-700 dark:text-amber-300 text-xs font-semibold hover:bg-pink-500/30"
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
                class="px-2 py-0.5 rounded bg-white/80 dark:bg-white/10 text-slate-600 dark:text-slate-300 hover:text-pink-600 dark:hover:text-amber-300 transition-colors"
              >
                + {{ p.label }}
              </button>
            </div>
          </div>
        </div>

        <!-- 提交发射按钮 -->
        <button
          type="submit"
          :disabled="publishing || !newContent.trim()"
          class="w-full py-2.5 px-4 rounded-xl bg-gradient-to-r from-pink-500 via-rose-500 to-amber-500 dark:from-amber-400 dark:via-rose-500 dark:to-nebula-cyan text-white text-xs font-bold shadow-lg shadow-pink-500/25 dark:shadow-amber-500/25 hover:opacity-95 active:scale-[0.99] disabled:opacity-50 disabled:pointer-events-none transition-all flex items-center justify-center space-x-1.5"
        >
          <Loader2 v-if="publishing" class="w-4 h-4 animate-spin" />
          <Sparkles v-else class="w-4 h-4" />
          <span>{{ publishing ? '发射中...' : '发射星际碎语 🚀' }}</span>
        </button>
      </form>

      <div v-if="postError" class="text-xs text-rose-500 dark:text-rose-400 font-mono">
        {{ postError }}
      </div>
    </div>

    <!-- 加载中 -->
    <div v-if="loading" class="text-center py-16 text-slate-500 dark:text-slate-400 text-sm">
      <div class="inline-block w-6 h-6 border-2 border-pink-500 dark:border-nebula-purple border-t-transparent rounded-full animate-spin mb-2"></div>
      <p>正在接收历史星际信号...</p>
    </div>

    <!-- 碎语时间线列表 -->
    <div v-else class="space-y-6 relative before:absolute before:inset-0 before:left-5 before:w-0.5 before:bg-pink-200/60 dark:before:bg-white/10">
      <div
        v-for="moment in moments"
        :key="moment.id"
        class="relative flex items-start space-x-4 group"
      >
        <!-- 时间线锚点 -->
        <div class="w-10 h-10 rounded-full glass-card border border-pink-200/70 dark:border-white/20 flex items-center justify-center text-lg shrink-0 shadow-lg group-hover:border-pink-400 dark:group-hover:border-nebula-purple/60 group-hover:scale-110 transition-all z-10 bg-white/90 dark:bg-space-950">
          {{ moment.moodEmoji || moment.mood || '✨' }}
        </div>

        <!-- 碎语卡片内容 -->
        <div class="flex-1 glass-card rounded-2xl p-5 border border-pink-200/60 dark:border-white/10 space-y-3 group-hover:border-pink-300 dark:group-hover:border-nebula-purple/30 transition-all shadow-md">
          <div class="flex items-center justify-between text-xs text-slate-500 dark:text-slate-400 font-mono">
            <span v-if="moment.location" class="flex items-center space-x-1 text-slate-500 dark:text-slate-400">
              <MapPin class="w-3 h-3 text-pink-500 dark:text-nebula-pink" />
              <span>{{ moment.location }}</span>
            </span>
            <span v-else>星向电台</span>
            <span>{{ formatTime(moment.createdAt) }}</span>
          </div>

          <!-- 正文 -->
          <p class="text-sm text-slate-700 dark:text-slate-200 leading-relaxed whitespace-pre-wrap">
            {{ moment.content }}
          </p>

          <!-- 朋友圈经典自适应九宫格图片展示 -->
          <div v-if="getMomentImages(moment).length > 0" class="pt-1">
            <!-- 单图大图展示 -->
            <div
              v-if="getMomentImages(moment).length === 1"
              class="max-w-md max-h-80 rounded-2xl overflow-hidden border border-pink-200/60 dark:border-white/10 shadow-lg cursor-pointer group/img relative"
              @click="openLightbox(getMomentImages(moment), 0)"
            >
              <img
                :src="getMomentImages(moment)[0]"
                alt="Moment photo"
                class="w-full h-full object-cover transition-transform duration-300 group-hover/img:scale-105"
                loading="lazy"
                decoding="async"
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
                class="aspect-square rounded-xl overflow-hidden border border-pink-200/60 dark:border-white/10 shadow cursor-pointer group/img relative bg-pink-50/50 dark:bg-space-900"
                @click="openLightbox(getMomentImages(moment), imgIdx)"
              >
                <img
                  :src="img"
                  alt="Moment photo"
                  class="w-full h-full object-cover transition-transform duration-300 group-hover/img:scale-110"
                  loading="lazy"
                  decoding="async"
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
                class="aspect-square rounded-xl overflow-hidden border border-pink-200/60 dark:border-white/10 shadow cursor-pointer group/img relative bg-pink-50/50 dark:bg-space-900"
                @click="openLightbox(getMomentImages(moment), imgIdx)"
              >
                <img
                  :src="img"
                  alt="Moment photo"
                  class="w-full h-full object-cover transition-transform duration-300 group-hover/img:scale-110"
                  loading="lazy"
                  decoding="async"
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
  Loader2,
  Image as ImageIcon,
  Link2,
  X,
  ChevronLeft,
  ChevronRight
} from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { getMoments, createMoment } from '@/api/moments';
import type { Moment } from '@/types';
import { useToast } from '@/composables/useToast';

const toast = useToast();
const isAdmin = ref(!!localStorage.getItem('starward_admin_token'));
const moments = ref<Moment[]>([]);
const loading = ref(true);
const publishing = ref(false);
const postError = ref('');

const newContent = ref('');
const selectedMood = ref('🚀');
const customLocation = ref('星穹列车 · 观景车厢');
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

// 配图相关响应式变量
const selectedImages = ref<string[]>([]);
const showUrlInput = ref(false);
const imageUrlInput = ref('');
const fileInputRef = ref<HTMLInputElement | null>(null);

const presetImages = [
  { label: '三月七自拍', url: '/images/hsr/march7th_selfie.webp' },
  { label: '姬子与列车', url: '/images/hsr/himeko_express.webp' },
  { label: '丹恒', url: '/images/hsr/danheng.webp' },
  { label: '瓦尔特', url: '/images/hsr/welt.webp' },
  { label: '帕姆列车长', url: '/images/pompom.webp' },
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

  if (!isAdmin.value) {
    toast.warning('当前为访客模式，仅列车长登录后可向全宇宙发射广播 🔑');
    return;
  }

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
      colors: ['#38bdf8', '#f59e0b', '#ec4899', '#10b981']
    });

    toast.success('星际碎语发射成功 ✨');

    newContent.value = '';
    selectedImages.value = [];
    showUrlInput.value = false;
    imageUrlInput.value = '';

    await loadMoments();
  } catch (err: any) {
    const msg = err.response?.data?.message || err.message || '发布失败';
    postError.value = msg;
    toast.error(msg);
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
