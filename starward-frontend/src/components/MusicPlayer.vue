<template>
  <div class="fixed bottom-6 right-6 z-50 select-none">
    <!-- 展开状态卡片 -->
    <transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="transform scale-95 opacity-0 translate-y-4"
      enter-to-class="transform scale-100 opacity-100 translate-y-0"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="transform scale-100 opacity-100 translate-y-0"
      leave-to-class="transform scale-95 opacity-0 translate-y-4"
    >
      <div
        v-if="musicStore.isExpanded"
        class="glass-island w-80 p-5 rounded-3xl shadow-2xl mb-3 border border-white/15 overflow-hidden relative"
      >
        <!-- 背景弥散微光 -->
        <div
          class="absolute -top-12 -right-12 w-32 h-32 rounded-full blur-2xl opacity-40 pointer-events-none"
          :style="{ backgroundImage: `url(${musicStore.currentSong.cover})`, backgroundSize: 'cover' }"
        ></div>

        <!-- 顶栏关闭按钮 -->
        <div class="flex items-center justify-between mb-4">
          <div class="flex items-center space-x-2">
            <span class="w-2 h-2 rounded-full bg-nebula-cyan animate-pulse"></span>
            <span class="text-xs font-mono tracking-wider text-slate-400 uppercase">Starward Radio</span>
          </div>
          <button
            @click="musicStore.isExpanded = false"
            class="text-slate-400 hover:text-white p-1 rounded-lg hover:bg-white/10 transition-colors"
          >
            <ChevronDown class="w-4 h-4" />
          </button>
        </div>

        <!-- 歌曲封面与黑胶旋转特效 -->
        <div class="flex items-center space-x-4 mb-4">
          <div class="relative group cursor-pointer" @click="musicStore.togglePlay">
            <div
              class="w-16 h-16 rounded-full overflow-hidden border-2 border-white/20 shadow-xl"
              :class="{ 'animate-spin-slow': musicStore.isPlaying }"
              :style="{ animationPlayState: musicStore.isPlaying ? 'running' : 'paused' }"
            >
              <img :src="musicStore.currentSong.cover" :alt="musicStore.currentSong.title" class="w-full h-full object-cover" />
            </div>
            <!-- 黑胶唱片中心圆孔 -->
            <div class="absolute inset-0 m-auto w-4 h-4 rounded-full bg-space-950 border border-white/30 flex items-center justify-center">
              <div class="w-1.5 h-1.5 rounded-full bg-white/70"></div>
            </div>
          </div>

          <div class="flex-1 min-w-0">
            <h4 class="text-sm font-bold text-white truncate">{{ musicStore.currentSong.title }}</h4>
            <p class="text-xs text-slate-400 truncate">{{ musicStore.currentSong.artist }}</p>
            
            <!-- 律动音波动画 -->
            <div class="flex items-end space-x-1 h-3 mt-2">
              <span
                v-for="i in 5"
                :key="i"
                class="w-1 bg-gradient-to-t from-nebula-cyan to-nebula-purple rounded-full transition-all duration-300"
                :style="{
                  height: musicStore.isPlaying ? `${Math.sin(i * 1.5 + Date.now() / 300) * 8 + 8}px` : '3px'
                }"
              ></span>
            </div>
          </div>
        </div>

        <!-- 进度条 -->
        <div class="space-y-1 mb-4">
          <div
            class="h-1.5 w-full bg-white/10 rounded-full overflow-hidden cursor-pointer relative"
            @click="handleSeek"
            ref="progressTrackRef"
          >
            <div
              class="h-full bg-gradient-to-r from-nebula-cyan to-nebula-purple rounded-full relative"
              :style="{ width: `${progressPercent}%` }"
            ></div>
          </div>
          <div class="flex justify-between text-[10px] font-mono text-slate-500">
            <span>{{ formatTime(musicStore.currentTime) }}</span>
            <span>{{ formatTime(musicStore.duration) }}</span>
          </div>
        </div>

        <!-- 控制按钮组 -->
        <div class="flex items-center justify-center space-x-5">
          <button @click="musicStore.prev" class="text-slate-400 hover:text-white transition-colors">
            <SkipBack class="w-4 h-4" />
          </button>
          <button
            @click="musicStore.togglePlay"
            class="w-10 h-10 rounded-full bg-gradient-to-tr from-nebula-cyan to-nebula-purple flex items-center justify-center text-white shadow-lg shadow-nebula-cyan/25 hover:scale-105 active:scale-95 transition-transform"
          >
            <Play v-if="!musicStore.isPlaying" class="w-4 h-4 ml-0.5" />
            <Pause v-else class="w-4 h-4" />
          </button>
          <button @click="musicStore.next" class="text-slate-400 hover:text-white transition-colors">
            <SkipForward class="w-4 h-4" />
          </button>
        </div>

        <!-- 歌单快速选择列表 -->
        <div class="mt-4 pt-3 border-t border-white/10 space-y-1.5 max-h-32 overflow-y-auto pr-1">
          <div
            v-for="(song, idx) in musicStore.playlist"
            :key="song.id"
            @click="musicStore.switchSong(idx)"
            class="flex items-center justify-between p-1.5 rounded-lg text-xs cursor-pointer transition-colors"
            :class="musicStore.currentIndex === idx ? 'bg-white/10 text-nebula-cyan' : 'text-slate-400 hover:text-slate-200 hover:bg-white/5'"
          >
            <span class="truncate">{{ song.title }} - {{ song.artist }}</span>
            <span v-if="musicStore.currentIndex === idx && musicStore.isPlaying" class="text-[10px] font-mono">▶</span>
          </div>
        </div>
      </div>
    </transition>

    <!-- 迷你胶囊岛 (默认悬浮状态) -->
    <div
      v-if="!musicStore.isExpanded"
      @click="musicStore.isExpanded = true"
      class="glass-island px-3.5 py-2 rounded-full cursor-pointer flex items-center space-x-3 hover:border-nebula-cyan/40 transition-all duration-300 shadow-xl group hover:shadow-nebula-cyan/15"
    >
      <!-- 迷你微缩旋转黑胶 -->
      <div
        class="w-8 h-8 rounded-full overflow-hidden border border-white/20 shadow-md relative"
        :class="{ 'animate-spin-slow': musicStore.isPlaying }"
        :style="{ animationPlayState: musicStore.isPlaying ? 'running' : 'paused' }"
      >
        <img :src="musicStore.currentSong.cover" :alt="musicStore.currentSong.title" class="w-full h-full object-cover" />
      </div>

      <!-- 歌曲信息 -->
      <div class="flex flex-col pr-1">
        <span class="text-xs font-medium text-slate-100 max-w-[110px] truncate group-hover:text-nebula-cyan transition-colors">
          {{ musicStore.currentSong.title }}
        </span>
        <span class="text-[10px] text-slate-400 font-mono -mt-0.5">
          {{ musicStore.isPlaying ? 'Playing...' : 'Paused' }}
        </span>
      </div>

      <!-- 快速播放/暂停控制 -->
      <button
        @click.stop="musicStore.togglePlay"
        class="w-7 h-7 rounded-full bg-white/10 hover:bg-nebula-cyan/20 text-slate-200 hover:text-nebula-cyan flex items-center justify-center transition-all"
      >
        <Play v-if="!musicStore.isPlaying" class="w-3.5 h-3.5 ml-0.5" />
        <Pause v-else class="w-3.5 h-3.5" />
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { Play, Pause, SkipBack, SkipForward, ChevronDown } from 'lucide-vue-next';
import { useMusicStore } from '@/stores/music';

const musicStore = useMusicStore();
const progressTrackRef = ref<HTMLElement | null>(null);

const progressPercent = computed(() => {
  if (!musicStore.duration) return 0;
  return (musicStore.currentTime / musicStore.duration) * 100;
});

const formatTime = (seconds: number) => {
  if (!seconds || isNaN(seconds)) return '00:00';
  const mins = Math.floor(seconds / 60);
  const secs = Math.floor(seconds % 60);
  return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
};

const handleSeek = (e: MouseEvent) => {
  if (!progressTrackRef.value || !musicStore.duration) return;
  const rect = progressTrackRef.value.getBoundingClientRect();
  const clickX = e.clientX - rect.left;
  const percent = Math.max(0, Math.min(1, clickX / rect.width));
  musicStore.seek(percent * musicStore.duration);
};
</script>
