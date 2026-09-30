<template>
  <div class="space-y-4">
    <!-- 顶部操作栏 -->
    <div class="glass-card p-4 rounded-2xl border border-pink-200/50 dark:border-white/10 flex flex-wrap items-center justify-between gap-4">
      <div class="flex items-center space-x-3">
        <router-link
          to="/admin/posts"
          class="p-2 rounded-xl border border-pink-200/60 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 bg-white/60 dark:bg-white/5 text-slate-600 dark:text-slate-300 hover:text-slate-900 dark:hover:text-white transition-colors flex items-center space-x-1"
        >
          <ArrowLeft class="w-4 h-4" />
          <span class="text-xs">返回列表</span>
        </router-link>

        <div>
          <h2 class="text-base font-bold text-slate-800 dark:text-white flex items-center space-x-2">
            <span>{{ isEditMode ? '编辑星际文章' : '撰写新星际文章' }}</span>
            <span v-if="isEditMode" class="text-xs font-mono text-amber-500 dark:text-amber-400">#{{ postId }}</span>
          </h2>
          <p class="text-[11px] text-slate-500 dark:text-slate-400 font-mono">
            支持实时 Markdown、Mermaid 架构图与代码高亮
          </p>
        </div>
      </div>

      <!-- 右侧视图切换与保存发布按钮 -->
      <div class="flex items-center space-x-3">
        <!-- 视图切换器 -->
        <div class="hidden sm:flex items-center p-1 rounded-xl bg-pink-100/70 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-xs">
          <button
            type="button"
            @click="viewMode = 'split'"
            class="px-2.5 py-1 rounded-lg transition-colors flex items-center space-x-1"
            :class="viewMode === 'split' ? 'bg-amber-500/20 text-amber-700 dark:text-amber-300 font-bold' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
            title="双栏并排编辑与实时预览"
          >
            <Columns class="w-3.5 h-3.5" />
            <span>双栏</span>
          </button>
          <button
            type="button"
            @click="viewMode = 'editor'"
            class="px-2.5 py-1 rounded-lg transition-colors flex items-center space-x-1"
            :class="viewMode === 'editor' ? 'bg-amber-500/20 text-amber-700 dark:text-amber-300 font-bold' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
            title="纯净编辑模式"
          >
            <PenTool class="w-3.5 h-3.5" />
            <span>纯编辑</span>
          </button>
          <button
            type="button"
            @click="viewMode = 'preview'"
            class="px-2.5 py-1 rounded-lg transition-colors flex items-center space-x-1"
            :class="viewMode === 'preview' ? 'bg-amber-500/20 text-amber-700 dark:text-amber-300 font-bold' : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'"
            title="纯渲染预览模式"
          >
            <Eye class="w-3.5 h-3.5" />
            <span>纯预览</span>
          </button>
        </div>

        <!-- 保存为草稿 -->
        <button
          type="button"
          @click="submitForm('DRAFT')"
          :disabled="submitting"
          class="px-3.5 py-2 rounded-xl border border-amber-400/30 bg-amber-500/10 hover:bg-amber-500/20 text-amber-300 text-xs font-semibold transition-all flex items-center space-x-1.5"
        >
          <Save class="w-4 h-4" />
          <span>存为草稿</span>
        </button>

        <!-- 立即公开发布 -->
        <button
          type="button"
          @click="submitForm('PUBLISHED')"
          :disabled="submitting"
          class="px-4 py-2 rounded-xl bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan text-white text-xs font-bold shadow-lg shadow-amber-500/25 hover:opacity-95 transition-all flex items-center space-x-1.5"
        >
          <Loader2 v-if="submitting" class="w-4 h-4 animate-spin" />
          <Sparkles v-else class="w-4 h-4" />
          <span>{{ isEditMode ? '更新发布' : '立即公开发布' }}</span>
        </button>
      </div>
    </div>

    <!-- 文章元数据设置抽屉/折叠卡片 -->
    <div class="glass-card p-5 rounded-2xl border border-pink-200/50 dark:border-white/10 space-y-4">
      <div class="flex items-center justify-between">
        <span class="text-xs font-semibold text-slate-700 dark:text-slate-300 flex items-center space-x-1.5">
          <Settings class="w-4 h-4 text-amber-500 dark:text-amber-400" />
          <span>文章基础元数据 (Metadata)</span>
        </span>
        <button
          type="button"
          @click="metaExpanded = !metaExpanded"
          class="text-xs text-slate-500 dark:text-slate-400 hover:text-amber-600 dark:hover:text-amber-400 transition-colors flex items-center space-x-1"
        >
          <span>{{ metaExpanded ? '收起设置' : '展开设置' }}</span>
          <ChevronDown class="w-3.5 h-3.5 transition-transform" :class="{ 'rotate-180': metaExpanded }" />
        </button>
      </div>

      <!-- 核心必填：标题与 Slug（常驻显示） -->
      <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div class="md:col-span-2">
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
            文章标题 (Title) <span class="text-rose-500">*</span>
          </label>
          <input
            v-model="form.title"
            type="text"
            placeholder="例如：星际拓荒记：从苍穹外卖到星向空间"
            class="w-full px-3.5 py-2.5 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-sm text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all font-medium"
            @blur="autoGenerateSlug"
          />
        </div>

        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1 flex items-center justify-between">
            <span>路由 Slug (唯一标识) <span class="text-rose-500">*</span></span>
            <button
              type="button"
              @click="generateSlugFromTitle"
              class="text-[10px] text-amber-600 dark:text-amber-400 hover:underline"
            >
              一键生成
            </button>
          </label>
          <input
            v-model="form.slug"
            type="text"
            placeholder="from-takeaway-to-starward"
            class="w-full px-3.5 py-2.5 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-sm text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all font-mono"
          />
        </div>
      </div>

      <!-- 展开设置：摘要、封面、标签、置顶 -->
      <div v-show="metaExpanded" class="space-y-4 pt-2 border-t border-pink-200/40 dark:border-white/5">
        <!-- 摘要 -->
        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1">
            文章摘要 (Summary)
          </label>
          <textarea
            v-model="form.summary"
            rows="2"
            placeholder="简要概括本文的核心亮点或思考感悟（若为空将自动截取正文前 120 字）..."
            class="w-full px-3.5 py-2 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all"
          ></textarea>
        </div>

        <!-- 封面图设置与预设 -->
        <div class="space-y-2">
          <div class="flex items-center justify-between">
            <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 flex items-center space-x-1.5">
              <ImageIcon class="w-3.5 h-3.5 text-pink-500 dark:text-nebula-cyan" />
              <span>文章封面设置 (Cover Image)</span>
            </label>
            <div class="flex items-center space-x-2">
              <span class="text-[10px] text-slate-500 dark:text-slate-400">支持拖拽图片入内 · 16:10 实机裁切取景</span>
            </div>
          </div>

          <!-- 封面操作主控制栏 -->
          <div
            class="p-3 rounded-2xl bg-white/70 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 flex flex-col sm:flex-row items-stretch sm:items-center gap-3 transition-colors"
            @dragover.prevent
            @drop.prevent="handleCoverDrop"
          >
            <!-- 缩略图视窗（点击亦可直接调起裁切） -->
            <div
              @click="openCropperWithCurrent"
              class="relative w-full sm:w-28 aspect-[16/10] rounded-xl bg-pink-100 dark:bg-space-900 border border-pink-200/60 dark:border-white/10 overflow-hidden shrink-0 cursor-pointer group shadow-sm"
              title="点击打开可视化裁切调整"
            >
              <img
                v-if="form.coverImage"
                :src="form.coverImage"
                alt="Preview"
                class="w-full h-full object-cover group-hover:scale-105 transition-transform"
                onerror="this.src='/images/hsr/himeko_express.png'"
              />
              <div class="absolute inset-0 bg-black/45 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center text-white text-[10px] font-medium space-x-1">
                <Crop class="w-3.5 h-3.5" />
                <span>裁切调整</span>
              </div>
            </div>

            <!-- URL 输入框与功能按钮组 -->
            <div class="flex-1 flex flex-col sm:flex-row items-stretch sm:items-center gap-2">
              <input
                v-model="form.coverImage"
                type="text"
                placeholder="https://... 或 /images/hsr/himeko_express.png"
                class="flex-1 px-3.5 py-2 rounded-xl bg-white/80 dark:bg-space-900 border border-pink-200/60 dark:border-white/10 text-xs text-slate-800 dark:text-slate-100 placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 transition-all font-mono"
              />

              <!-- 隐藏的本地文件上传 input -->
              <input
                ref="coverFileInputRef"
                type="file"
                accept="image/*"
                class="hidden"
                @change="handleCoverFileSelected"
              />

              <input
                ref="rawCoverFileInputRef"
                type="file"
                accept="image/*"
                class="hidden"
                @change="handleDirectUploadOriginal"
              />

              <div class="flex items-center space-x-2 shrink-0">
                <!-- 直接上传原图 (文章内竖图竖放自适应展示) -->
                <button
                  type="button"
                  @click="triggerRawCoverUpload"
                  :disabled="uploadingRawCover"
                  class="px-3 py-2 rounded-xl border border-pink-200/70 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 bg-white/80 dark:bg-white/5 text-slate-700 dark:text-slate-300 text-xs font-medium transition-colors flex items-center space-x-1.5"
                  title="直接上传无损原始图片（不强制横向切片，文章内将以完整原画竖放展示）"
                >
                  <Loader2 v-if="uploadingRawCover" class="w-3.5 h-3.5 animate-spin" />
                  <ImageIcon v-else class="w-3.5 h-3.5 text-pink-500 dark:text-nebula-cyan" />
                  <span>直接上传原图</span>
                </button>

                <!-- 上传本地图片并裁切按钮 -->
                <button
                  type="button"
                  @click="triggerCoverUpload"
                  class="px-3 py-2 rounded-xl bg-gradient-to-r from-pink-500 to-rose-500 hover:from-pink-600 hover:to-rose-600 text-white text-xs font-semibold shadow-md shadow-pink-500/20 transition-all flex items-center space-x-1.5"
                  title="选择本地任意图片并调起 16:10 构图工坊微调取景区块"
                >
                  <Upload class="w-3.5 h-3.5" />
                  <span>上传并裁切</span>
                </button>

                <!-- 裁切微调现有图片 -->
                <button
                  type="button"
                  @click="openCropperWithCurrent"
                  :disabled="!form.coverImage"
                  class="px-3 py-2 rounded-xl border border-pink-200/70 dark:border-white/10 hover:border-pink-300 dark:hover:border-white/20 bg-white/80 dark:bg-white/5 text-slate-700 dark:text-slate-300 text-xs font-medium transition-colors flex items-center space-x-1.5 disabled:opacity-40"
                  title="对当前封面打开 16:10 构图调整"
                >
                  <Crop class="w-3.5 h-3.5 text-pink-500 dark:text-nebula-cyan" />
                  <span>构图微调</span>
                </button>
              </div>
            </div>
          </div>

          <!-- 快速预设按钮 -->
          <div class="flex items-center space-x-2 pt-1">
            <span class="text-[11px] text-slate-500 dark:text-slate-400 shrink-0">快捷壁纸：</span>
            <div class="flex flex-wrap gap-1.5">
              <button
                v-for="preset in coverPresets"
                :key="preset.name"
                type="button"
                @click="form.coverImage = preset.url"
                class="px-2 py-0.5 rounded-lg bg-white/60 dark:bg-white/5 hover:bg-pink-100/60 dark:hover:bg-white/10 border border-pink-200/60 dark:border-white/10 text-[11px] text-slate-600 dark:text-slate-300 hover:text-slate-900 dark:hover:text-white transition-colors"
                :class="{ 'border-pink-400 dark:border-nebula-cyan text-pink-600 dark:text-nebula-cyan font-medium': form.coverImage === preset.url }"
              >
                {{ preset.name }}
              </button>
            </div>
          </div>
        </div>

        <!-- 标签多选与置顶开关 -->
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4 pt-2">
          <!-- 标签多选 -->
          <div>
            <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1.5 flex items-center justify-between">
              <span>选择所属标签 (Tags)</span>
              <router-link to="/admin/tags" class="text-[10px] text-pink-600 dark:text-nebula-cyan hover:underline">
                管理标签库 →
              </router-link>
            </label>
            <div class="flex flex-wrap gap-1.5 p-2 rounded-xl bg-white/70 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 min-h-[42px]">
              <button
                v-for="tag in allTags"
                :key="tag.id"
                type="button"
                @click="toggleTagSelection(tag.id)"
                class="px-2.5 py-1 rounded-lg text-xs font-mono border transition-all flex items-center space-x-1"
                :style="isTagSelected(tag.id) ? {
                  backgroundColor: `${tag.color || '#6366f1'}30`,
                  color: tag.color || '#6366f1',
                  borderColor: tag.color || '#6366f1'
                } : {
                  backgroundColor: 'transparent',
                  color: '#64748b',
                  borderColor: 'rgba(244, 114, 182, 0.3)'
                }"
              >
                <Check v-if="isTagSelected(tag.id)" class="w-3 h-3" />
                <span>{{ tag.name }}</span>
              </button>
              <span v-if="allTags.length === 0" class="text-xs text-slate-500 py-1 px-2">暂无标签</span>
            </div>
          </div>

          <!-- 置顶与状态选项 -->
          <div class="flex items-center space-x-6 p-3 rounded-xl bg-white/70 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10">
            <!-- 置顶开关 -->
            <label class="flex items-center space-x-2.5 cursor-pointer">
              <input
                v-model="form.isPinned"
                type="checkbox"
                class="w-4 h-4 rounded text-amber-500 focus:ring-amber-400 bg-white dark:bg-space-900 border-slate-300 dark:border-white/20"
              />
              <span class="text-xs font-medium text-slate-800 dark:text-slate-200 flex items-center space-x-1">
                <Pin class="w-3.5 h-3.5 text-amber-500 dark:text-amber-400" />
                <span>置顶此文章</span>
              </span>
            </label>

            <!-- 状态 -->
            <div class="text-xs flex items-center space-x-2">
              <span class="text-slate-500 dark:text-slate-400">初始状态:</span>
              <span
                class="px-2 py-0.5 rounded text-[11px] font-medium"
                :class="form.status === 'PUBLISHED' ? 'bg-emerald-500/20 text-emerald-700 dark:text-emerald-300' : 'bg-amber-500/20 text-amber-700 dark:text-amber-300'"
              >
                {{ form.status === 'PUBLISHED' ? '公开发布' : '草稿' }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Markdown 工具栏 -->
    <div class="glass-card px-4 py-2 rounded-xl border border-pink-200/50 dark:border-white/10 flex flex-wrap items-center gap-1.5 text-xs">
      <button
        v-for="tool in markdownTools"
        :key="tool.title"
        type="button"
        @click="insertMarkdown(tool.prefix, tool.suffix, tool.defaultText)"
        class="p-1.5 rounded-lg hover:bg-pink-100/70 dark:hover:bg-white/10 text-slate-600 dark:text-slate-300 hover:text-slate-900 dark:hover:text-white transition-colors"
        :title="tool.title"
      >
        <component :is="tool.icon" class="w-4 h-4" />
      </button>

      <span class="w-[1px] h-4 bg-pink-200/60 dark:bg-white/10 mx-1"></span>

      <!-- 模板快捷插入 -->
      <button
        type="button"
        @click="insertMermaidTemplate"
        class="px-2 py-1 rounded-lg bg-nebula-cyan/10 hover:bg-nebula-cyan/20 text-sky-600 dark:text-nebula-cyan border border-sky-400/30 dark:border-nebula-cyan/30 text-[11px] font-mono transition-colors"
        title="插入 Mermaid 流程架构图模板"
      >
        + Mermaid 架构图
      </button>

      <button
        type="button"
        @click="insertAlertTemplate"
        class="px-2 py-1 rounded-lg bg-amber-400/10 hover:bg-amber-400/20 text-amber-600 dark:text-amber-300 border border-amber-400/30 text-[11px] font-mono transition-colors"
        title="插入 GitHub 风格星轨提示框"
      >
        + 提示引用框
      </button>

      <div class="ml-auto text-[11px] text-slate-500 dark:text-slate-400 font-mono hidden md:flex items-center space-x-3">
        <span>字数: {{ form.contentMd.length }}</span>
        <span>快捷键: Cmd+S / Ctrl+S 保存</span>
      </div>
    </div>

    <!-- 双栏编辑器主体 -->
    <div class="grid gap-4" :class="{
      'grid-cols-1 md:grid-cols-2': viewMode === 'split',
      'grid-cols-1': viewMode !== 'split'
    }">
      <!-- 左栏：源码编辑器 -->
      <div
        v-show="viewMode === 'split' || viewMode === 'editor'"
        class="glass-card rounded-2xl border border-pink-200/50 dark:border-white/10 overflow-hidden flex flex-col h-[650px] shadow-lg"
      >
        <div class="px-4 py-2 bg-pink-500/5 dark:bg-white/5 border-b border-pink-200/40 dark:border-white/5 flex items-center justify-between text-xs text-slate-600 dark:text-slate-400 font-mono">
          <span class="flex items-center space-x-1.5">
            <Code class="w-3.5 h-3.5 text-amber-500 dark:text-amber-400" />
            <span>Markdown 源码编辑器</span>
          </span>
          <span class="text-[10px] text-slate-500">支持 Tab 缩进</span>
        </div>
        <textarea
          ref="textareaRef"
          v-model="form.contentMd"
          class="flex-1 w-full p-4 bg-white/80 dark:bg-[#080b12] text-slate-800 dark:text-slate-200 text-sm font-mono leading-relaxed resize-none focus:outline-none selection:bg-amber-400/30 selection:text-slate-900 dark:selection:text-white placeholder-slate-400 dark:placeholder-slate-600 transition-colors"
          placeholder="在此尽情挥洒星际灵感... 愿此行，终抵群星 ✦"
          @keydown="handleKeydown"
        ></textarea>
      </div>

      <!-- 右栏：实时渲染预览 -->
      <div
        v-show="viewMode === 'split' || viewMode === 'preview'"
        class="glass-card rounded-2xl border border-pink-200/50 dark:border-white/10 overflow-hidden flex flex-col h-[650px] shadow-lg"
      >
        <div class="px-4 py-2 bg-pink-500/5 dark:bg-white/5 border-b border-pink-200/40 dark:border-white/5 flex items-center justify-between text-xs text-slate-600 dark:text-slate-400 font-mono">
          <span class="flex items-center space-x-1.5">
            <Eye class="w-3.5 h-3.5 text-pink-600 dark:text-nebula-cyan" />
            <span>实时渲染视口 (Live Preview)</span>
          </span>
          <span class="text-[10px] text-slate-500">Mermaid · 代码高亮实时同步</span>
        </div>
        <div class="flex-1 p-6 overflow-y-auto bg-white/70 dark:bg-space-950/60">
          <MarkdownViewer :content="previewContent || '*暂无内容，请在左侧编辑器中输入 Markdown 正文...*'" />
        </div>
      </div>
    </div>

    <!-- 封面图可视化交互式裁切与实机预览模态框 -->
    <CoverCropperModal
      v-model:show="showCropperModal"
      :initial-image="cropperImageSource"
      :raw-file="currentRawCoverFile"
      :article-title="form.title"
      :article-summary="form.summary"
      :tags="selectedTagNames"
      @crop-success="handleCropSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted, onUnmounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import {
  ArrowLeft,
  Columns,
  PenTool,
  Eye,
  Save,
  Sparkles,
  Loader2,
  Settings,
  ChevronDown,
  Check,
  Pin,
  Code,
  Bold,
  Italic,
  Heading1,
  Heading2,
  Quote,
  List,
  Link as LinkIcon,
  Image as ImageIcon,
  Table as TableIcon,
  Upload,
  Crop
} from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import MarkdownViewer from '@/components/MarkdownViewer.vue';
import CoverCropperModal from '@/components/admin/CoverCropperModal.vue';
import {
  getPostForEdit,
  createPost,
  updatePost,
  getAllTags,
  uploadImage
} from '@/api/admin';
import type { Tag, PostCreateRequest, PostUpdateRequest } from '@/types';
import { useToast } from '@/composables/useToast';

const route = useRoute();
const router = useRouter();
const toast = useToast();

const postId = computed(() => {
  const p = route.params.id;
  return p ? Number(p) : null;
});
const isEditMode = computed(() => !!postId.value);

const viewMode = ref<'split' | 'editor' | 'preview'>('split');
const metaExpanded = ref(true);
const submitting = ref(false);
const textareaRef = ref<HTMLTextAreaElement | null>(null);

const allTags = ref<Tag[]>([]);

const form = reactive<{
  title: string;
  slug: string;
  summary: string;
  contentMd: string;
  coverImage: string;
  status: 'PUBLISHED' | 'DRAFT';
  isPinned: boolean;
  tagIds: number[];
}>({
  title: '',
  slug: '',
  summary: '',
  contentMd: '',
  coverImage: '/images/hsr/himeko_express.png',
  status: 'PUBLISHED',
  isPinned: false,
  tagIds: [],
});

const previewContent = ref(form.contentMd);
let previewDebounceTimer: ReturnType<typeof setTimeout> | null = null;

watch(() => form.contentMd, (val) => {
  if (previewDebounceTimer) clearTimeout(previewDebounceTimer);
  previewDebounceTimer = setTimeout(() => {
    previewContent.value = val;
  }, 100);
});

const coverPresets = [
  { name: '姬子·星穹列车', url: '/images/hsr/himeko_express.png' },
  { name: '三月七·自拍手记', url: '/images/hsr/march7th_selfie.png' },
  { name: '丹恒·击云长枪', url: '/images/hsr/danheng.png' },
  { name: '瓦尔特·以世界之名', url: '/images/hsr/welt.png' },
  { name: '流萤·深空之夜', url: '/images/hsr/firefly_night.jpg' },
  { name: '绯英·落樱晨曦', url: '/images/hsr/feiying_sakura.png' },
  { name: '星穹列车·站台', url: '/images/hsr/astral_express_bg.jpg' },
  { name: '璀璨深空星云', url: 'https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?auto=format&fit=crop&w=1200&q=80' },
  { name: '赛博霓虹星轨', url: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=1200&q=80' },
];

const markdownTools = [
  { title: '加粗 (Bold)', icon: Bold, prefix: '**', suffix: '**', defaultText: '加粗文字' },
  { title: '斜体 (Italic)', icon: Italic, prefix: '*', suffix: '*', defaultText: '斜体文字' },
  { title: '一级标题 (H1)', icon: Heading1, prefix: '# ', suffix: '', defaultText: '一级标题' },
  { title: '二级标题 (H2)', icon: Heading2, prefix: '## ', suffix: '', defaultText: '二级标题' },
  { title: '引用 (Quote)', icon: Quote, prefix: '> ', suffix: '', defaultText: '引用文本' },
  { title: '行内代码 (Code)', icon: Code, prefix: '`', suffix: '`', defaultText: 'code' },
  { title: '无序列表 (List)', icon: List, prefix: '- ', suffix: '', defaultText: '列表项' },
  { title: '插入超链接 (Link)', icon: LinkIcon, prefix: '[', suffix: '](https://)', defaultText: '链接文本' },
  { title: '插入图片 (Image)', icon: ImageIcon, prefix: '![', suffix: '](https://)', defaultText: '图片描述' },
  { title: '插入表格 (Table)', icon: TableIcon, prefix: '| 列1 | 列2 |\n| :--- | :--- |\n| 内容1 | 内容2 |', suffix: '', defaultText: '' },
];

const isTagSelected = (tagId: number) => form.tagIds.includes(tagId);

const toggleTagSelection = (tagId: number) => {
  if (isTagSelected(tagId)) {
    form.tagIds = form.tagIds.filter(id => id !== tagId);
  } else {
    form.tagIds.push(tagId);
  }
};

// 封面裁切弹窗与本地上传状态
const showCropperModal = ref(false);
const cropperImageSource = ref('');
const currentRawCoverFile = ref<File | null>(null);
const coverFileInputRef = ref<HTMLInputElement | null>(null);
const rawCoverFileInputRef = ref<HTMLInputElement | null>(null);
const uploadingRawCover = ref(false);

const selectedTagNames = computed(() => {
  return allTags.value
    .filter(t => form.tagIds.includes(t.id))
    .map(t => t.name);
});

const triggerCoverUpload = () => {
  coverFileInputRef.value?.click();
};

const triggerRawCoverUpload = () => {
  rawCoverFileInputRef.value?.click();
};

const handleCoverFileSelected = (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  if (!file.type.startsWith('image/')) {
    toast.warning('请选择图片格式文件 (JPG, PNG, WEBP 等)');
    return;
  }

  currentRawCoverFile.value = file;
  const reader = new FileReader();
  reader.onload = (event) => {
    cropperImageSource.value = event.target?.result as string;
    showCropperModal.value = true;
  };
  reader.readAsDataURL(file);
  target.value = '';
};

// 直接上传无损原始大图（文章内自适应竖放/横放，列表卡片自动聚焦头部）
const handleDirectUploadOriginal = async (e: Event) => {
  const target = e.target as HTMLInputElement;
  const file = target.files?.[0];
  if (!file) return;

  if (!file.type.startsWith('image/')) {
    toast.warning('请选择图片格式文件');
    return;
  }

  uploadingRawCover.value = true;
  try {
    const result = await uploadImage(file, file.name);
    form.coverImage = result.url;
    toast.success('原始图片上传成功 ✨ 文章内将以自适应竖版/横版原画展现！');
  } catch (err: any) {
    console.error('上传原图失败:', err);
    toast.error(err.response?.data?.message || err.message || '上传原图失败');
  } finally {
    uploadingRawCover.value = false;
    target.value = '';
  }
};

const handleCoverDrop = (e: DragEvent) => {
  const file = e.dataTransfer?.files?.[0];
  if (!file) return;

  if (!file.type.startsWith('image/')) {
    toast.warning('请拖拽图片格式文件');
    return;
  }

  currentRawCoverFile.value = file;
  const reader = new FileReader();
  reader.onload = (event) => {
    cropperImageSource.value = event.target?.result as string;
    showCropperModal.value = true;
  };
  reader.readAsDataURL(file);
};

const openCropperWithCurrent = () => {
  currentRawCoverFile.value = null;
  cropperImageSource.value = form.coverImage || '/images/hsr/himeko_express.png';
  showCropperModal.value = true;
};

const handleCropSuccess = (newUrl: string) => {
  form.coverImage = newUrl;
};

const autoGenerateSlug = () => {
  if (!form.slug && form.title) {
    generateSlugFromTitle();
  }
};

const generateSlugFromTitle = () => {
  if (!form.title.trim()) return;
  // 简易拼音/英文 Slug 规则：去除标点，空格转中划线
  let s = form.title
    .toLowerCase()
    .replace(/[^\w\s\u4e00-\u9fa5-]/g, '')
    .trim()
    .replace(/\s+/g, '-');
  if (!s) s = `post-${Date.now()}`;
  form.slug = s;
};

// 插入 Markdown 语法
const insertMarkdown = (prefix: string, suffix: string, defaultText: string) => {
  const el = textareaRef.value;
  if (!el) return;

  const start = el.selectionStart;
  const end = el.selectionEnd;
  const selectedText = el.value.substring(start, end) || defaultText;
  const replacement = `${prefix}${selectedText}${suffix}`;

  form.contentMd = el.value.substring(0, start) + replacement + el.value.substring(end);

  setTimeout(() => {
    el.focus();
    el.setSelectionRange(start + prefix.length, start + prefix.length + selectedText.length);
  }, 50);
};

const insertMermaidTemplate = () => {
  const template = `\n\`\`\`mermaid
flowchart TD
    A["🚀 星向空间起航"] --> B["⚡ 全栈核心架构"]
    B --> C["🎨 二次元极客美学"]
    B --> D["🛡️ 安全鉴权与管理"]
    C --> E["✦ 终抵群星 ✦"]
    D --> E
\`\`\`\n`;
  insertMarkdown('', '', template);
};

const insertAlertTemplate = () => {
  const template = `\n> [!NOTE]
> 愿此行，终抵群星 ✦\n`;
  insertMarkdown('', '', template);
};

// 处理键盘事件（Tab 缩进与 Cmd+S 保存）
const handleKeydown = (e: KeyboardEvent) => {
  if (e.key === 'Tab') {
    e.preventDefault();
    const el = textareaRef.value;
    if (!el) return;
    const start = el.selectionStart;
    const end = el.selectionEnd;
    form.contentMd = el.value.substring(0, start) + '  ' + el.value.substring(end);
    setTimeout(() => {
      el.selectionStart = el.selectionEnd = start + 2;
    }, 0);
  }
};

const handleGlobalKeydown = (e: KeyboardEvent) => {
  if ((e.metaKey || e.ctrlKey) && e.key === 's') {
    e.preventDefault();
    submitForm(form.status);
  }
};

// 表单提交
const submitForm = async (targetStatus?: 'PUBLISHED' | 'DRAFT') => {
  if (!form.title.trim()) {
    toast.warning('请填写文章标题');
    return;
  }
  if (!form.slug.trim()) {
    toast.warning('请填写文章 Slug');
    return;
  }
  if (!form.contentMd.trim()) {
    toast.warning('请填写 Markdown 正文');
    return;
  }

  const finalStatus = targetStatus || form.status;
  submitting.value = true;

  try {
    const payload: PostCreateRequest | PostUpdateRequest = {
      title: form.title.trim(),
      slug: form.slug.trim(),
      summary: form.summary.trim() || form.contentMd.slice(0, 120),
      contentMd: form.contentMd,
      coverImage: form.coverImage.trim() || undefined,
      status: finalStatus,
      isPinned: form.isPinned ? 1 : 0,
      tagIds: form.tagIds,
    };

    if (isEditMode.value && postId.value) {
      await updatePost(postId.value, payload);
    } else {
      await createPost(payload as PostCreateRequest);
    }

    confetti({
      particleCount: 50,
      spread: 60,
      origin: { y: 0.5 },
      colors: ['#38bdf8', '#f59e0b', '#ec4899', '#10b981'],
    });

    toast.success(isEditMode.value ? '文章更新成功 ✨' : '文章创建并发布成功 🚀');
    router.push('/admin/posts');
  } catch (err: any) {
    toast.error(err.response?.data?.message || err.message || '操作失败，请重试');
  } finally {
    submitting.value = false;
  }
};

// 初始化数据
const initData = async () => {
  try {
    const tags = await getAllTags();
    allTags.value = tags || [];
  } catch (err) {
    console.error('获取标签库失败:', err);
  }

  if (isEditMode.value && postId.value) {
    try {
      const detail = await getPostForEdit(postId.value);
      form.title = detail.title || '';
      form.slug = detail.slug || '';
      form.summary = detail.summary || '';
      form.contentMd = detail.contentMd || detail.content || '';
      previewContent.value = form.contentMd;
      form.coverImage = detail.coverImage || '';
      form.status = (detail.status || 'PUBLISHED').toUpperCase() as 'PUBLISHED' | 'DRAFT';
      form.isPinned = detail.isPinned === true || detail.isPinned === 1;
      form.tagIds = detail.tags ? detail.tags.map(t => t.id) : [];
    } catch (err: any) {
      toast.error(err.response?.data?.message || err.message || '加载文章数据失败');
      router.push('/admin/posts');
    }
  }
};

onMounted(() => {
  initData();
  window.addEventListener('keydown', handleGlobalKeydown);
});

onUnmounted(() => {
  window.removeEventListener('keydown', handleGlobalKeydown);
});
</script>
