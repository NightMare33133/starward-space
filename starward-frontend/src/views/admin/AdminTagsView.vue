<template>
  <div class="space-y-6">
    <!-- 顶部说明与新建标签面板 -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
      <!-- 左侧：新建标签卡片 -->
      <div class="glass-card p-6 rounded-2xl border border-white/10 space-y-4">
        <div class="flex items-center space-x-2">
          <div class="p-2 rounded-xl bg-amber-400/10 text-amber-400 border border-amber-400/20">
            <Plus class="w-4 h-4" />
          </div>
          <div>
            <h3 class="text-sm font-bold text-white">创建新星轨标签</h3>
            <p class="text-[11px] text-slate-400">为星向空间文章构建专属分类体系</p>
          </div>
        </div>

        <form @submit.prevent="handleCreateTag" class="space-y-4">
          <!-- 标签名称 -->
          <div>
            <label class="block text-xs font-medium text-slate-300 mb-1">
              标签名称 <span class="text-rose-400">*</span>
            </label>
            <input
              v-model="newTag.name"
              type="text"
              placeholder="例如：星穹铁道"
              class="w-full px-3.5 py-2 rounded-xl bg-space-950/80 border border-white/10 text-xs text-slate-100 placeholder-slate-500 focus:outline-none focus:border-amber-400/80 transition-all"
              @blur="autoSlug"
            />
          </div>

          <!-- Slug -->
          <div>
            <label class="block text-xs font-medium text-slate-300 mb-1">
              标签 Slug (唯一标识) <span class="text-rose-400">*</span>
            </label>
            <input
              v-model="newTag.slug"
              type="text"
              placeholder="例如：hsr"
              class="w-full px-3.5 py-2 rounded-xl bg-space-950/80 border border-white/10 text-xs text-slate-100 placeholder-slate-500 focus:outline-none focus:border-amber-400/80 transition-all font-mono"
            />
          </div>

          <!-- 主题色选取 -->
          <div>
            <label class="block text-xs font-medium text-slate-300 mb-1.5 flex items-center justify-between">
              <span>专属主题霓虹色</span>
              <span class="font-mono text-[10px] text-slate-400">{{ newTag.color }}</span>
            </label>
            <div class="flex items-center space-x-2">
              <input
                v-model="newTag.color"
                type="color"
                class="w-8 h-8 rounded-lg bg-transparent border-0 cursor-pointer p-0"
              />
              <div class="flex-1 flex flex-wrap gap-1.5">
                <button
                  v-for="color in presetColors"
                  :key="color"
                  type="button"
                  @click="newTag.color = color"
                  class="w-6 h-6 rounded-md border transition-transform hover:scale-110"
                  :style="{ backgroundColor: color, borderColor: newTag.color === color ? '#fff' : 'transparent' }"
                ></button>
              </div>
            </div>
          </div>

          <!-- 提交按钮 -->
          <button
            type="submit"
            :disabled="creating || !newTag.name.trim() || !newTag.slug.trim()"
            class="w-full py-2.5 rounded-xl bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan text-white text-xs font-bold shadow-lg shadow-amber-500/20 hover:opacity-95 disabled:opacity-50 transition-all flex items-center justify-center space-x-1.5"
          >
            <Loader2 v-if="creating" class="w-3.5 h-3.5 animate-spin" />
            <Sparkles v-else class="w-3.5 h-3.5" />
            <span>{{ creating ? '创建中...' : '确认添加标签' }}</span>
          </button>
        </form>
      </div>

      <!-- 右侧：现有标签列表 -->
      <div class="md:col-span-2 glass-card p-6 rounded-2xl border border-white/10 space-y-4">
        <div class="flex items-center justify-between">
          <div class="flex items-center space-x-2">
            <TagIcon class="w-4 h-4 text-nebula-cyan" />
            <h3 class="text-sm font-bold text-white">已有星轨标签 ({{ tags.length }})</h3>
          </div>
          <button
            @click="fetchTags"
            :disabled="loading"
            class="p-1.5 rounded-lg border border-white/10 hover:border-white/20 bg-white/5 text-slate-300 hover:text-white transition-colors"
            title="刷新"
          >
            <RefreshCw class="w-3.5 h-3.5" :class="{ 'animate-spin': loading }" />
          </button>
        </div>

        <!-- 标签加载状态 -->
        <div v-if="loading" class="py-12 text-center text-slate-400 flex flex-col items-center justify-center space-y-2">
          <Loader2 class="w-5 h-5 animate-spin text-amber-400" />
          <span class="text-xs font-mono">加载标签中...</span>
        </div>

        <div v-else-if="tags.length === 0" class="py-12 text-center text-slate-500 text-xs">
          暂无标签，请在左侧创建
        </div>

        <!-- 标签网格 -->
        <div v-else class="grid grid-cols-1 sm:grid-cols-2 gap-3">
          <div
            v-for="tag in tags"
            :key="tag.id"
            class="p-3.5 rounded-xl bg-space-950/70 border border-white/10 flex items-center justify-between group hover:border-white/20 transition-all"
          >
            <div class="flex items-center space-x-3">
              <span
                class="w-3.5 h-3.5 rounded-full shrink-0 shadow-sm"
                :style="{ backgroundColor: tag.color || '#6366f1' }"
              ></span>
              <div>
                <p class="text-xs font-bold text-slate-100 flex items-center space-x-1.5">
                  <span>{{ tag.name }}</span>
                </p>
                <p class="text-[10px] text-slate-500 font-mono mt-0.5">
                  Slug: {{ tag.slug }}
                </p>
              </div>
            </div>

            <!-- 删除标签 -->
            <button
              @click="handleDeleteTag(tag)"
              class="opacity-0 group-hover:opacity-100 p-1.5 rounded-lg text-slate-500 hover:text-rose-400 hover:bg-rose-500/10 transition-all"
              title="删除标签"
            >
              <Trash2 class="w-3.5 h-3.5" />
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import {
  Tag as TagIcon,
  Plus,
  Trash2,
  RefreshCw,
  Sparkles,
  Loader2
} from 'lucide-vue-next';
import { getAllTags, createTag, deleteTag } from '@/api/admin';
import type { Tag } from '@/types';
import { useToast } from '@/composables/useToast';

const toast = useToast();
const tags = ref<Tag[]>([]);
const loading = ref(false);
const creating = ref(false);

const presetColors = [
  '#38bdf8', // 霓虹青
  '#10b981', // 翡翠绿
  '#f59e0b', // 琥珀金
  '#ec4899', // 霓虹粉
  '#a855f7', // 星云紫
  '#6366f1', // 深空靛
  '#f43f5e', // 战歌红
];

const newTag = reactive({
  name: '',
  slug: '',
  color: '#38bdf8',
});

const autoSlug = () => {
  if (!newTag.slug && newTag.name) {
    newTag.slug = newTag.name
      .toLowerCase()
      .replace(/[^\w\s-]/g, '')
      .trim()
      .replace(/\s+/g, '-');
  }
};

const fetchTags = async () => {
  loading.value = true;
  try {
    const list = await getAllTags();
    tags.value = list || [];
  } catch (err: any) {
    console.error('获取标签失败:', err);
  } finally {
    loading.value = false;
  }
};

const handleCreateTag = async () => {
  if (!newTag.name.trim() || !newTag.slug.trim()) return;

  creating.value = true;
  try {
    const created = await createTag({
      name: newTag.name.trim(),
      slug: newTag.slug.trim(),
      color: newTag.color,
    });
    tags.value.push(created);
    newTag.name = '';
    newTag.slug = '';
    toast.success('标签创建成功 ✨');
  } catch (err: any) {
    toast.error(err.response?.data?.message || err.message || '创建标签失败');
  } finally {
    creating.value = false;
  }
};

const handleDeleteTag = async (tag: Tag) => {
  if (confirm(`确定要删除标签「${tag.name}」吗？解绑文章不会删除文章本身。`)) {
    try {
      await deleteTag(tag.id);
      tags.value = tags.value.filter(t => t.id !== tag.id);
      toast.success('标签已删除 🗑️');
    } catch (err: any) {
      toast.error(err.response?.data?.message || err.message || '删除标签失败');
    }
  }
};

onMounted(() => {
  fetchTags();
});
</script>
