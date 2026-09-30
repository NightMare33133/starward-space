<template>
  <div
    v-if="show"
    class="fixed inset-0 z-50 flex items-center justify-center p-3 sm:p-6 bg-slate-950/75 backdrop-blur-md overflow-y-auto animate-fade-in"
    @click.self="handleClose"
  >
    <div
      class="w-full max-w-5xl rounded-3xl glass-card border border-pink-200/50 dark:border-white/10 bg-white/95 dark:bg-space-950/95 shadow-2xl overflow-hidden flex flex-col max-h-[92vh]"
      role="dialog"
      aria-modal="true"
    >
      <!-- 模态框顶部导航 -->
      <div class="px-6 py-4 border-b border-pink-200/40 dark:border-white/10 flex items-center justify-between bg-pink-500/5 dark:bg-white/5">
        <div class="flex items-center space-x-2.5">
          <div class="p-2 rounded-xl bg-pink-100 dark:bg-pink-900/30 text-pink-600 dark:text-nebula-cyan">
            <Crop class="w-5 h-5" />
          </div>
          <div>
            <h3 class="text-base font-bold text-slate-800 dark:text-white flex items-center space-x-2">
              <span>封面视觉工坊 · 交互裁切与实机预览</span>
              <span class="text-[11px] px-2 py-0.5 rounded-full bg-pink-100 text-pink-700 dark:bg-nebula-cyan/20 dark:text-nebula-cyan font-mono">16:10 宽屏基准</span>
            </h3>
            <p class="text-xs text-slate-500 dark:text-slate-400">
              按住鼠标拖拽取景、滚轮缩放，右侧即时呈现前台卡片实机效果（所见即所得）
            </p>
          </div>
        </div>

        <button
          type="button"
          @click="handleClose"
          class="p-2 rounded-xl text-slate-400 hover:text-slate-700 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-white/10 transition-colors"
          title="关闭窗口"
        >
          <X class="w-5 h-5" />
        </button>
      </div>

      <!-- 核心工作区：双栏布局 (左操作台 + 右实机预览) -->
      <div class="p-6 overflow-y-auto flex-1 grid grid-cols-1 lg:grid-cols-12 gap-6">
        <!-- 左侧：交互裁切工作台 (7 cols) -->
        <div class="lg:col-span-7 flex flex-col space-y-4">
          <div class="flex items-center justify-between text-xs text-slate-600 dark:text-slate-300">
            <span class="font-semibold flex items-center space-x-1.5">
              <Move class="w-3.5 h-3.5 text-pink-500 dark:text-nebula-cyan" />
              <span>裁切取景框 (拖拽平移 / 滚轮缩放)</span>
            </span>

            <div class="flex items-center space-x-2">
              <button
                type="button"
                @click="showGrid = !showGrid"
                class="px-2 py-1 rounded-lg border text-[11px] transition-colors flex items-center space-x-1"
                :class="showGrid ? 'bg-pink-100/80 text-pink-700 border-pink-300 dark:bg-nebula-cyan/20 dark:text-nebula-cyan dark:border-nebula-cyan/40' : 'bg-transparent text-slate-500 border-slate-300 dark:border-white/10'"
                title="开启/关闭构图九宫格参考线"
              >
                <Grid3X3 class="w-3 h-3" />
                <span>九宫格参考线</span>
              </button>
            </div>
          </div>

          <!-- 裁切视窗容器 (严格 16:10 约束比例) -->
          <div
            ref="cropperContainerRef"
            class="relative w-full aspect-[16/10] rounded-2xl overflow-hidden bg-slate-950 select-none shadow-2xl border-2 border-pink-300/60 dark:border-nebula-cyan/40 cursor-grab active:cursor-grabbing touch-none group"
            @pointerdown="handlePointerDown"
            @pointermove="handlePointerMove"
            @pointerup="handlePointerUp"
            @pointercancel="handlePointerUp"
            @wheel.prevent="handleWheel"
          >
            <!-- 底层被拖拽缩放的原图 -->
            <img
              v-if="imageSrc"
              ref="imageRef"
              :src="imageSrc"
              alt="Crop Source"
              crossorigin="anonymous"
              @load="onImageLoaded"
              class="absolute pointer-events-none max-w-none origin-top-left will-change-transform"
              :style="{
                width: `${displayedWidth}px`,
                height: `${displayedHeight}px`,
                transform: `translate3d(${posX}px, ${posY}px, 0)`
              }"
            />

            <!-- 九宫格参考线蒙层 (黄金分割构图辅助) -->
            <div
              v-if="showGrid && imageLoaded"
              class="absolute inset-0 pointer-events-none grid grid-cols-3 grid-rows-3"
            >
              <div class="border-r border-b border-white/25"></div>
              <div class="border-r border-b border-white/25"></div>
              <div class="border-b border-white/25"></div>
              <div class="border-r border-b border-white/25"></div>
              <div class="border-r border-b border-white/25"></div>
              <div class="border-b border-white/25"></div>
              <div class="border-r border-white/25"></div>
              <div class="border-r border-white/25"></div>
              <div></div>
            </div>

            <!-- 浮动操作手势指引 -->
            <div
              v-if="!imageLoaded"
              class="absolute inset-0 flex flex-col items-center justify-center text-slate-400 text-xs bg-slate-900/90 space-y-2"
            >
              <Loader2 class="w-6 h-6 animate-spin text-pink-500 dark:text-nebula-cyan" />
              <span>正在载入图片高分辨率数据...</span>
            </div>

            <div
              v-else
              class="absolute bottom-2.5 right-2.5 px-2.5 py-1 rounded-full text-[10px] font-mono shadow backdrop-blur-md bg-black/60 text-white/90 border border-white/10 opacity-70 group-hover:opacity-100 transition-opacity pointer-events-none"
            >
              取景尺寸: {{ Math.round(sourceW) }} × {{ Math.round(sourceH) }} px
            </div>
          </div>

          <!-- 缩放与快速对齐工具条 -->
          <div class="p-3.5 rounded-2xl bg-pink-50/70 dark:bg-space-900/60 border border-pink-200/50 dark:border-white/10 space-y-3">
            <!-- 缩放滑块 -->
            <div class="flex items-center space-x-3 text-xs">
              <span class="text-slate-600 dark:text-slate-400 shrink-0 font-medium flex items-center space-x-1">
                <ZoomIn class="w-3.5 h-3.5 text-pink-500 dark:text-nebula-cyan" />
                <span>缩放倍率</span>
              </span>
              <button
                type="button"
                @click="adjustZoom(-0.1)"
                class="p-1.5 rounded-lg hover:bg-white/80 dark:hover:bg-white/10 text-slate-600 dark:text-slate-300 transition-colors"
                title="缩小"
              >
                <ZoomOut class="w-3.5 h-3.5" />
              </button>
              <input
                type="range"
                :min="1"
                :max="3"
                step="0.02"
                v-model.number="zoom"
                @input="applyZoom"
                class="flex-1 accent-pink-500 dark:accent-nebula-cyan cursor-pointer h-1.5 bg-slate-200 dark:bg-space-950 rounded-lg"
              />
              <button
                type="button"
                @click="adjustZoom(0.1)"
                class="p-1.5 rounded-lg hover:bg-white/80 dark:hover:bg-white/10 text-slate-600 dark:text-slate-300 transition-colors"
                title="放大"
              >
                <ZoomIn class="w-3.5 h-3.5" />
              </button>
              <span class="font-mono text-xs w-12 text-right text-pink-600 dark:text-nebula-cyan font-bold">
                {{ Math.round(zoom * 100) }}%
              </span>
            </div>

            <!-- 针对立绘/竖图特化的快捷对齐定位按钮 -->
            <div class="flex flex-wrap items-center justify-between gap-2 pt-1 border-t border-pink-200/40 dark:border-white/5">
              <span class="text-[11px] text-slate-500 dark:text-slate-400">快捷取景定位：</span>
              <div class="flex items-center space-x-1.5">
                <button
                  type="button"
                  @click="alignPosition('top')"
                  class="px-2.5 py-1 rounded-lg text-xs bg-white/80 dark:bg-white/5 hover:bg-pink-100/60 dark:hover:bg-white/10 border border-pink-200/60 dark:border-white/10 text-slate-700 dark:text-slate-300 transition-colors flex items-center space-x-1"
                  title="聚焦头部/上方（适合角色竖版立绘）"
                >
                  <ArrowUp class="w-3 h-3 text-pink-500 dark:text-nebula-cyan" />
                  <span>面部/顶部</span>
                </button>
                <button
                  type="button"
                  @click="alignPosition('center')"
                  class="px-2.5 py-1 rounded-lg text-xs bg-white/80 dark:bg-white/5 hover:bg-pink-100/60 dark:hover:bg-white/10 border border-pink-200/60 dark:border-white/10 text-slate-700 dark:text-slate-300 transition-colors flex items-center space-x-1"
                  title="居中对齐"
                >
                  <AlignCenter class="w-3 h-3 text-pink-500 dark:text-nebula-cyan" />
                  <span>正中</span>
                </button>
                <button
                  type="button"
                  @click="alignPosition('bottom')"
                  class="px-2.5 py-1 rounded-lg text-xs bg-white/80 dark:bg-white/5 hover:bg-pink-100/60 dark:hover:bg-white/10 border border-pink-200/60 dark:border-white/10 text-slate-700 dark:text-slate-300 transition-colors flex items-center space-x-1"
                  title="底部对齐"
                >
                  <ArrowDown class="w-3 h-3 text-pink-500 dark:text-nebula-cyan" />
                  <span>底部</span>
                </button>
                <button
                  type="button"
                  @click="resetView"
                  class="px-2.5 py-1 rounded-lg text-xs bg-white/80 dark:bg-white/5 hover:bg-pink-100/60 dark:hover:bg-white/10 border border-pink-200/60 dark:border-white/10 text-slate-700 dark:text-slate-300 transition-colors flex items-center space-x-1"
                  title="恢复初始视角"
                >
                  <RotateCcw class="w-3 h-3 text-amber-500" />
                  <span>重置</span>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 右侧：实机双卡片实时联动渲染预览 (5 cols) -->
        <div class="lg:col-span-5 flex flex-col space-y-4">
          <div class="flex items-center justify-between text-xs text-slate-600 dark:text-slate-300">
            <span class="font-semibold flex items-center space-x-1.5">
              <Eye class="w-3.5 h-3.5 text-pink-500 dark:text-nebula-cyan" />
              <span>前台实机展示效果 (边调边看)</span>
            </span>
            <span class="text-[10px] px-2 py-0.5 rounded-full bg-emerald-100 text-emerald-700 dark:bg-emerald-950/60 dark:text-emerald-300 font-mono">
              实时同步 60 FPS
            </span>
          </div>

          <div class="space-y-4 flex-1 overflow-y-auto pr-1">
            <!-- 预览卡片 1：明信片网格卡片 (CuteLeaf 拍立得模式) -->
            <div class="glass-card rounded-2xl p-3.5 border border-pink-200/60 dark:border-white/10 shadow-md">
              <div class="text-[11px] font-bold text-slate-500 dark:text-slate-400 mb-2 flex items-center justify-between">
                <span>① 明信片网格模式 (Postcard Grid)</span>
                <span class="text-[10px] font-mono text-pink-600 dark:text-nebula-cyan">比例 16:10</span>
              </div>

              <!-- 卡片微缩实体 -->
              <div class="rounded-2xl border border-pink-200/40 dark:border-white/10 bg-white/90 dark:bg-space-950/80 p-3 overflow-hidden shadow-sm">
                <!-- 封面图渲染：通过纯百分比严密映射裁切视口 -->
                <div class="relative w-full aspect-[16/10] rounded-xl overflow-hidden bg-slate-900 mb-2.5 select-none shadow-sm">
                  <img
                    v-if="imageSrc"
                    :src="imageSrc"
                    alt="Preview Postcard"
                    class="absolute pointer-events-none max-w-none origin-top-left"
                    :style="previewImageStyle"
                  />
                  <div class="absolute inset-0 bg-gradient-to-t from-slate-950/60 via-transparent to-transparent opacity-40"></div>

                  <!-- 浮动置顶胶囊 -->
                  <div class="absolute top-2 left-2 flex items-center space-x-1 px-2 py-0.5 rounded-full text-[9px] font-bold shadow backdrop-blur-md bg-gradient-to-r from-amber-500 to-rose-500 text-white">
                    <Bookmark class="w-2.5 h-2.5 fill-current" />
                    <span>置顶</span>
                  </div>

                  <!-- 预估时长 -->
                  <div class="absolute top-2 right-2 px-2 py-0.5 rounded-full text-[9px] font-mono shadow backdrop-blur-md bg-black/50 text-white/90 border border-white/20 flex items-center space-x-1">
                    <Clock class="w-2.5 h-2.5 text-pink-300 dark:text-nebula-cyan" />
                    <span>约 3 分钟</span>
                  </div>
                </div>

                <!-- 标题与模拟摘要 -->
                <div class="space-y-1">
                  <h4 class="text-xs font-bold text-slate-800 dark:text-white line-clamp-1">
                    {{ articleTitle || '星向空间：文章标题实时呈现预览' }}
                  </h4>
                  <p class="text-[10px] text-slate-500 dark:text-slate-400 line-clamp-1">
                    {{ articleSummary || '文章正文摘要或感悟... 所见即所得，精准审视焦点。' }}
                  </p>
                </div>

                <!-- 标签与底栏 -->
                <div class="mt-2 pt-2 border-t border-pink-200/40 dark:border-white/5 flex items-center justify-between text-[10px] text-slate-400">
                  <div class="flex items-center space-x-1">
                    <span
                      v-for="(tag, idx) in previewTags"
                      :key="idx"
                      class="px-1.5 py-0.5 rounded bg-pink-100/70 text-pink-700 dark:bg-white/5 dark:text-slate-300 text-[9px]"
                    >
                      #{{ tag }}
                    </span>
                  </div>
                  <span class="text-pink-600 dark:text-nebula-cyan font-semibold flex items-center space-x-0.5">
                    <span>阅读全文</span>
                    <ArrowRight class="w-2.5 h-2.5" />
                  </span>
                </div>
              </div>
            </div>

            <!-- 预览卡片 2：横向图文流卡片 (Magazine Feed 模式) -->
            <div class="glass-card rounded-2xl p-3.5 border border-pink-200/60 dark:border-white/10 shadow-md">
              <div class="text-[11px] font-bold text-slate-500 dark:text-slate-400 mb-2 flex items-center justify-between">
                <span>② 横向图文流模式 (Horizontal Feed)</span>
                <span class="text-[10px] font-mono text-pink-600 dark:text-nebula-cyan">比例 16:10</span>
              </div>

              <!-- 卡片微缩实体 -->
              <div class="rounded-2xl border border-pink-200/40 dark:border-white/10 bg-white/90 dark:bg-space-950/80 p-2.5 overflow-hidden flex items-center space-x-3 shadow-sm">
                <!-- 左侧微缩封面 -->
                <div class="relative w-28 shrink-0 aspect-[16/10] rounded-lg overflow-hidden bg-slate-900 select-none shadow-sm">
                  <img
                    v-if="imageSrc"
                    :src="imageSrc"
                    alt="Preview Feed"
                    class="absolute pointer-events-none max-w-none origin-top-left"
                    :style="previewImageStyle"
                  />
                  <div class="absolute inset-0 bg-gradient-to-t from-slate-950/50 via-transparent to-transparent opacity-30"></div>
                </div>

                <!-- 右侧标题与摘要 -->
                <div class="flex-1 min-w-0 space-y-1">
                  <div class="flex items-center space-x-1 text-[9px] font-mono text-slate-400">
                    <Calendar class="w-2.5 h-2.5 text-pink-500 dark:text-nebula-cyan" />
                    <span>2026-09-30</span>
                  </div>
                  <h4 class="text-xs font-bold text-slate-800 dark:text-white truncate">
                    {{ articleTitle || '星向空间：文章标题实时呈现预览' }}
                  </h4>
                  <p class="text-[10px] text-slate-500 dark:text-slate-400 line-clamp-1">
                    {{ articleSummary || '文章正文摘要或感悟... 所见即所得，精准审视焦点。' }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 底部操作栏 -->
      <div class="px-6 py-4 border-t border-pink-200/40 dark:border-white/10 flex flex-wrap items-center justify-between gap-3 bg-pink-500/5 dark:bg-white/5">
        <!-- 左侧隐藏式本地重新选图入口 -->
        <div>
          <input
            ref="reselectInputRef"
            type="file"
            accept="image/*"
            class="hidden"
            @change="handleReselectFile"
          />
          <button
            type="button"
            @click="triggerReselect"
            class="px-3.5 py-2 rounded-xl border border-pink-200/70 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 bg-white/70 dark:bg-white/5 text-slate-700 dark:text-slate-300 text-xs font-medium transition-colors flex items-center space-x-1.5"
          >
            <Upload class="w-4 h-4 text-pink-500 dark:text-nebula-cyan" />
            <span>更换本地图片</span>
          </button>
        </div>

        <!-- 右侧提交与取消按钮 -->
        <div class="flex flex-wrap items-center justify-end gap-2.5">
          <button
            type="button"
            @click="handleClose"
            :disabled="uploading"
            class="px-3.5 py-2 rounded-xl border border-slate-300 dark:border-white/10 text-slate-600 dark:text-slate-300 hover:bg-slate-100 dark:hover:bg-white/5 text-xs font-semibold transition-colors"
          >
            取消
          </button>

          <!-- 一键保留原始原画（竖图竖放自适应展台） -->
          <button
            type="button"
            @click="handleUploadOriginal"
            :disabled="uploading || !imageLoaded"
            class="px-4 py-2 rounded-xl border border-pink-300/80 dark:border-nebula-cyan/40 bg-pink-50 dark:bg-white/5 text-pink-700 dark:text-nebula-cyan hover:bg-pink-100/80 dark:hover:bg-white/10 text-xs font-semibold transition-colors flex items-center space-x-1.5 disabled:opacity-50"
            title="不进行切片，直接将无损原始图片作为封面（文章内将以完整原画竖放展示）"
          >
            <Maximize2 class="w-3.5 h-3.5" />
            <span>保留完整原图 (原画竖放)</span>
          </button>

          <!-- 裁切为 16:10 宽屏切片 -->
          <button
            type="button"
            @click="handleConfirmCrop"
            :disabled="uploading || !imageLoaded"
            class="px-5 py-2 rounded-xl bg-gradient-to-r from-pink-500 via-rose-500 to-nebula-cyan text-white text-xs font-bold shadow-lg shadow-pink-500/25 hover:opacity-95 transition-all flex items-center space-x-2 disabled:opacity-50"
          >
            <Loader2 v-if="uploading" class="w-4 h-4 animate-spin" />
            <Sparkles v-else class="w-4 h-4" />
            <span>{{ uploading ? '正在生成切片并上传...' : '确定截取为 16:10 封面' }}</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue';
import {
  Crop,
  X,
  Move,
  Grid3X3,
  ZoomIn,
  ZoomOut,
  ArrowUp,
  ArrowDown,
  AlignCenter,
  RotateCcw,
  Eye,
  Bookmark,
  Clock,
  Calendar,
  ArrowRight,
  Upload,
  Loader2,
  Sparkles,
  Maximize2
} from 'lucide-vue-next';
import { uploadImage } from '@/api/admin';
import { useToast } from '@/composables/useToast';

interface Props {
  show: boolean;
  initialImage?: string;
  rawFile?: File | null;
  articleTitle?: string;
  articleSummary?: string;
  tags?: string[];
}

const props = withDefaults(defineProps<Props>(), {
  show: false,
  initialImage: '',
  articleTitle: '',
  articleSummary: '',
  tags: () => ['星向架构', '探索札记']
});

const emit = defineEmits<{
  (e: 'update:show', value: boolean): void;
  (e: 'crop-success', url: string): void;
}>();

const toast = useToast();

const cropperContainerRef = ref<HTMLDivElement | null>(null);
const imageRef = ref<HTMLImageElement | null>(null);
const reselectInputRef = ref<HTMLInputElement | null>(null);

const imageSrc = ref<string>('');
const imageLoaded = ref<boolean>(false);
const uploading = ref<boolean>(false);
const showGrid = ref<boolean>(true);

// 真实原图自然尺寸
const naturalWidth = ref<number>(0);
const naturalHeight = ref<number>(0);

// 容器视口尺寸 (16:10)
const viewportWidth = ref<number>(480);
const viewportHeight = ref<number>(300);

// 变换参数
const zoom = ref<number>(1);
const baseScale = ref<number>(1);
const posX = ref<number>(0);
const posY = ref<number>(0);

// 拖拽手势状态
let isDragging = false;
let startPointerX = 0;
let startPointerY = 0;
let initialPosX = 0;
let initialPosY = 0;

const displayedWidth = computed(() => {
  return naturalWidth.value * baseScale.value * zoom.value;
});

const displayedHeight = computed(() => {
  return naturalHeight.value * baseScale.value * zoom.value;
});

// 计算在原图上的实际截取坐标 (Natural Coordinates)
const currentScale = computed(() => baseScale.value * zoom.value);

const sourceX = computed(() => {
  if (currentScale.value <= 0) return 0;
  return Math.max(0, -posX.value / currentScale.value);
});

const sourceY = computed(() => {
  if (currentScale.value <= 0) return 0;
  return Math.max(0, -posY.value / currentScale.value);
});

const sourceW = computed(() => {
  if (currentScale.value <= 0) return naturalWidth.value;
  return Math.min(naturalWidth.value, viewportWidth.value / currentScale.value);
});

const sourceH = computed(() => {
  if (currentScale.value <= 0) return naturalHeight.value;
  return Math.min(naturalHeight.value, viewportHeight.value / currentScale.value);
});

// 计算给预览卡片的百分比样式 (零延迟 60 FPS 瞬时映射)
const previewImageStyle = computed(() => {
  if (!naturalWidth.value || !naturalHeight.value || !sourceW.value || !sourceH.value) {
    return { width: '100%', height: '100%', left: '0%', top: '0%' };
  }
  const relX = sourceX.value / naturalWidth.value;
  const relY = sourceY.value / naturalHeight.value;
  const relW = sourceW.value / naturalWidth.value;
  const relH = sourceH.value / naturalHeight.value;

  return {
    width: `${(1 / relW) * 100}%`,
    height: `${(1 / relH) * 100}%`,
    left: `${(-relX / relW) * 100}%`,
    top: `${(-relY / relH) * 100}%`
  };
});

const previewTags = computed(() => {
  return props.tags && props.tags.length > 0 ? props.tags.slice(0, 3) : ['星向探索', '全栈'];
});

// 约束边界：严防出现黑边空隙
const clampPosition = () => {
  const minX = viewportWidth.value - displayedWidth.value;
  const minY = viewportHeight.value - displayedHeight.value;

  if (posX.value > 0) posX.value = 0;
  if (posX.value < minX) posX.value = minX;

  if (posY.value > 0) posY.value = 0;
  if (posY.value < minY) posY.value = minY;
};

// 图片加载完毕初始化布局
const onImageLoaded = () => {
  const img = imageRef.value;
  const container = cropperContainerRef.value;
  if (!img || !container) return;

  naturalWidth.value = img.naturalWidth;
  naturalHeight.value = img.naturalHeight;

  // 获取容器真实像素宽高
  const rect = container.getBoundingClientRect();
  viewportWidth.value = rect.width || 480;
  viewportHeight.value = rect.height || (rect.width * 10) / 16;

  // 计算充满 16:10 框的基准比例
  const scaleX = viewportWidth.value / naturalWidth.value;
  const scaleY = viewportHeight.value / naturalHeight.value;
  baseScale.value = Math.max(scaleX, scaleY);

  zoom.value = 1;

  // 默认定位：若是竖图，默认对齐面部/偏上方 (Y = 15% 处)
  if (naturalHeight.value > naturalWidth.value) {
    alignPosition('top');
  } else {
    alignPosition('center');
  }

  imageLoaded.value = true;
};

// 快捷对齐预设
const alignPosition = (alignment: 'top' | 'center' | 'bottom') => {
  const minX = viewportWidth.value - displayedWidth.value;
  const minY = viewportHeight.value - displayedHeight.value;

  posX.value = minX / 2; // 水平居中

  if (alignment === 'top') {
    // 竖图面部焦点：略微往下挪 15%，避免贴死顶边缘
    posY.value = minY * 0.15;
  } else if (alignment === 'center') {
    posY.value = minY / 2;
  } else if (alignment === 'bottom') {
    posY.value = minY;
  }

  clampPosition();
};

const resetView = () => {
  zoom.value = 1;
  alignPosition(naturalHeight.value > naturalWidth.value ? 'top' : 'center');
};

// 滚轮与滑块缩放
const adjustZoom = (delta: number) => {
  let newZoom = zoom.value + delta;
  if (newZoom < 1) newZoom = 1;
  if (newZoom > 3) newZoom = 3;
  zoom.value = Number(newZoom.toFixed(2));
  applyZoom();
};

const applyZoom = () => {
  clampPosition();
};

const handleWheel = (e: WheelEvent) => {
  const delta = e.deltaY < 0 ? 0.08 : -0.08;
  adjustZoom(delta);
};

// 拖拽手势交互
const handlePointerDown = (e: PointerEvent) => {
  if (!imageLoaded.value) return;
  isDragging = true;
  startPointerX = e.clientX;
  startPointerY = e.clientY;
  initialPosX = posX.value;
  initialPosY = posY.value;
  (e.target as HTMLElement).setPointerCapture?.(e.pointerId);
};

const handlePointerMove = (e: PointerEvent) => {
  if (!isDragging) return;
  const dx = e.clientX - startPointerX;
  const dy = e.clientY - startPointerY;

  posX.value = initialPosX + dx;
  posY.value = initialPosY + dy;
  clampPosition();
};

const handlePointerUp = (e: PointerEvent) => {
  if (isDragging) {
    isDragging = false;
    (e.target as HTMLElement).releasePointerCapture?.(e.pointerId);
  }
};

// 触发重新选图
const triggerReselect = () => {
  reselectInputRef.value?.click();
};

const selectedRawFile = ref<File | null>(null);

const handleReselectFile = (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  if (!file.type.startsWith('image/')) {
    toast.warning('请选择图片类型文件 (如 JPG, PNG, WEBP)');
    return;
  }

  selectedRawFile.value = file;
  const reader = new FileReader();
  reader.onload = (event) => {
    imageSrc.value = event.target?.result as string;
    imageLoaded.value = false;
  };
  reader.readAsDataURL(file);
  target.value = '';
};

// 一键保留完整原图上传 (不进行裁切，竖图竖放自适应)
const handleUploadOriginal = async () => {
  if (!imageLoaded.value) return;

  uploading.value = true;
  try {
    if (selectedRawFile.value) {
      const result = await uploadImage(selectedRawFile.value, selectedRawFile.value.name);
      toast.success('已保留原始插画大图 ✨ 文章内将自适应竖版原画展现！');
      emit('crop-success', result.url);
      handleClose();
      return;
    }

    // 若当前为已有图片或 URL，拉取为 blob 并保存为无损原图
    const res = await fetch(imageSrc.value);
    const blob = await res.blob();
    const ext = blob.type.includes('png') ? '.png' : blob.type.includes('jpeg') ? '.jpg' : '.webp';
    const result = await uploadImage(blob, `cover_original_${Date.now()}${ext}`);
    toast.success('已保留原始插画大图 ✨ 文章内将自适应竖版原画展现！');
    emit('crop-success', result.url);
    handleClose();
  } catch (err: any) {
    console.error('上传完整原图异常:', err);
    toast.error(err.response?.data?.message || err.message || '上传原图失败');
  } finally {
    uploading.value = false;
  }
};

// 确认截取并上传生成高清切片
const handleConfirmCrop = async () => {
  if (!imageLoaded.value || !imageRef.value) return;

  uploading.value = true;
  try {
    const canvas = document.createElement('canvas');
    // 输出高画质 16:10 宽屏切片 (1600 × 1000)
    const exportWidth = 1600;
    const exportHeight = 1000;
    canvas.width = exportWidth;
    canvas.height = exportHeight;

    const ctx = canvas.getContext('2d');
    if (!ctx) throw new Error('无法初始化 Canvas 绘图上下文');

    ctx.imageSmoothingEnabled = true;
    ctx.imageSmoothingQuality = 'high';

    ctx.drawImage(
      imageRef.value,
      sourceX.value,
      sourceY.value,
      sourceW.value,
      sourceH.value,
      0,
      0,
      exportWidth,
      exportHeight
    );

    // 导出为 WebP 高保真格式
    canvas.toBlob(async (blob) => {
      if (!blob) {
        uploading.value = false;
        toast.error('生成切片文件失败，请重试');
        return;
      }

      try {
        const result = await uploadImage(blob, `cover_${Date.now()}.webp`);
        toast.success('封面裁切并上传成功 ✨');
        emit('crop-success', result.url);
        handleClose();
      } catch (err: any) {
        console.error('上传裁切图片异常:', err);
        toast.error(err.response?.data?.message || err.message || '封面上传失败');
      } finally {
        uploading.value = false;
      }
    }, 'image/webp', 0.92);
  } catch (err: any) {
    uploading.value = false;
    console.error('Canvas 截取异常:', err);
    toast.error('裁切图片处理出现异常，跨域图片建议先保存到本地后上传');
  }
};

const handleClose = () => {
  if (uploading.value) return;
  emit('update:show', false);
};

// 监听打开弹窗
watch(
  () => props.show,
  (val) => {
    if (val) {
      selectedRawFile.value = props.rawFile || null;
      imageLoaded.value = false;
      imageSrc.value = props.initialImage || '/images/hsr/himeko_express.png';
      nextTick(() => {
        // 重置拖拽状态
        zoom.value = 1;
      });
    }
  },
  { immediate: true }
);
</script>

<style scoped>
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.98);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
.animate-fade-in {
  animation: fadeIn 0.2s cubic-bezier(0.16, 1, 0.3, 1);
}
</style>
