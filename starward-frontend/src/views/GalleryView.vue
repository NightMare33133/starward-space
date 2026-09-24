<template>
  <div class="max-w-6xl mx-auto px-4 sm:px-6 py-10 space-y-8">
    <!-- 头部介绍区 -->
    <div class="space-y-3 border-b border-white/10 pb-6">
      <h1 class="text-3xl font-extrabold text-white tracking-tight flex items-center space-x-3">
        <Camera class="w-8 h-8 text-nebula-pink" />
        <span>摄影视界 · Visual Odyssey</span>
      </h1>
      <p class="text-sm text-slate-400">
        代码之外，用镜头捕获光影流转与现实世界的温柔角落。
      </p>
    </div>

    <!-- 分类筛选器 -->
    <div class="flex items-center gap-2 flex-wrap">
      <button
        v-for="cat in categories"
        :key="cat"
        @click="selectedCategory = cat"
        class="px-4 py-1.5 rounded-full text-xs font-medium transition-all"
        :class="selectedCategory === cat ? 'bg-nebula-pink text-white font-bold shadow-md shadow-nebula-pink/25' : 'glass-card text-slate-400 hover:text-slate-200'"
      >
        {{ cat }}
      </button>
    </div>

    <!-- 摄影卡片瀑布流网格 -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="photo in filteredPhotos"
        :key="photo.id"
        class="glass-card rounded-2xl overflow-hidden group cursor-pointer border border-white/10 hover:border-nebula-pink/40 transition-all duration-500"
        @click="previewPhoto = photo"
      >
        <!-- 图片容器与悬浮缩放 -->
        <div class="relative aspect-[4/3] overflow-hidden bg-space-950">
          <img
            :src="photo.url"
            :alt="photo.title"
            loading="lazy"
            class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700 ease-out"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-space-950/80 via-transparent to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
          
          <!-- 悬停时右上角放大提示图标 -->
          <div class="absolute top-3 right-3 p-2 rounded-full glass-island opacity-0 group-hover:opacity-100 transition-opacity">
            <Maximize2 class="w-4 h-4 text-white" />
          </div>
        </div>

        <!-- 照片说明信息 -->
        <div class="p-4 space-y-2">
          <div class="flex items-center justify-between">
            <h3 class="text-sm font-bold text-white group-hover:text-nebula-pink transition-colors">
              {{ photo.title }}
            </h3>
            <span class="text-[10px] text-slate-500 font-mono">{{ photo.date }}</span>
          </div>

          <div class="flex items-center justify-between text-xs text-slate-400 font-mono pt-1">
            <span class="flex items-center space-x-1">
              <MapPin class="w-3 h-3 text-nebula-pink" />
              <span>{{ photo.location }}</span>
            </span>
            <span v-if="photo.params" class="text-[10px] text-slate-500">
              {{ photo.params }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 大图全屏预览模态框 (Lightbox Modal) -->
    <div
      v-if="previewPhoto"
      class="fixed inset-0 z-50 bg-black/90 backdrop-blur-xl flex items-center justify-center p-4 sm:p-8"
      @click="previewPhoto = null"
    >
      <div class="relative max-w-5xl max-h-[90vh] flex flex-col items-center" @click.stop>
        <!-- 关闭按钮 -->
        <button
          @click="previewPhoto = null"
          class="absolute -top-12 right-0 p-2 text-slate-400 hover:text-white transition-colors"
        >
          <X class="w-6 h-6" />
        </button>

        <img
          :src="previewPhoto.url"
          :alt="previewPhoto.title"
          class="max-w-full max-h-[80vh] object-contain rounded-xl shadow-2xl"
        />

        <div class="mt-4 text-center space-y-1">
          <h4 class="text-white font-bold text-base">{{ previewPhoto.title }}</h4>
          <p class="text-xs text-slate-400 font-mono">{{ previewPhoto.location }} · {{ previewPhoto.params }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { Camera, MapPin, Maximize2, X } from 'lucide-vue-next';
import type { Photo } from '@/types';

const selectedCategory = ref('全部');
const previewPhoto = ref<Photo | null>(null);

const categories = ['全部', '星空与宇宙', '城市建筑', '山川自然'];

const photos = ref<Photo[]>([
  {
    id: '1',
    title: '银河悬垂于寂静之夜',
    location: '莫干山云端',
    date: '2024.08',
    category: '星空与宇宙',
    url: 'https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=1200&auto=format&fit=crop&q=80',
    params: 'Sony A7M4 · 24mm f/1.4 · ISO 3200'
  },
  {
    id: '2',
    title: '海风拂过石狮黄金海岸',
    location: '福建泉州 · 石狮',
    date: '2024.07',
    category: '山川自然',
    url: 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1200&auto=format&fit=crop&q=80',
    params: 'Sony A7M4 · 35mm f/2.0 · 1/500s'
  },
  {
    id: '3',
    title: '赛博深蓝与维多利亚港的倒影',
    location: '中国香港 · 维港',
    date: '2024.09',
    category: '城市建筑',
    url: 'https://images.unsplash.com/photo-1506973035872-a4ec16b8e8d9?w=1200&auto=format&fit=crop&q=80',
    params: 'Sony A7M4 · 50mm f/1.8 · 1/60s'
  },
  {
    id: '4',
    title: '深空星云与光年的低语',
    location: '天文台深空拍摄',
    date: '2024.06',
    category: '星空与宇宙',
    url: 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=1200&auto=format&fit=crop&q=80',
    params: 'Telescope 800mm · Stacked'
  },
  {
    id: '5',
    title: '林间雾气与晨曦破晓',
    location: '武夷山脉',
    date: '2024.05',
    category: '山川自然',
    url: 'https://images.unsplash.com/photo-1448375240586-882707db888b?w=1200&auto=format&fit=crop&q=80',
    params: 'Sony A7M4 · 85mm f/1.8'
  },
  {
    id: '6',
    title: '暮色熔金的天际线',
    location: '上海 · 陆家嘴',
    date: '2024.04',
    category: '城市建筑',
    url: 'https://images.unsplash.com/photo-1513694203232-719a280e022f?w=1200&auto=format&fit=crop&q=80',
    params: 'Sony A7M4 · 24-70mm f/2.8'
  }
]);

const filteredPhotos = computed(() => {
  if (selectedCategory.value === '全部') return photos.value;
  return photos.value.filter(p => p.category === selectedCategory.value);
});
</script>
