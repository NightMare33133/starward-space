<template>
  <div class="max-w-6xl mx-auto px-4 sm:px-6 py-6 space-y-8 relative">
    <!-- 顶部动态弥散星云光晕 (随当前歌曲封面主色调变换) -->
    <div
      class="fixed inset-0 pointer-events-none opacity-25 blur-[100px] -z-10 transition-all duration-1000"
      :style="{
        backgroundImage: `radial-gradient(circle at 50% 30%, #38bdf8 0%, #ec4899 40%, transparent 70%)`
      }"
    ></div>

    <!-- 顶部标题 -->
    <div class="flex flex-col sm:flex-row sm:items-end justify-between gap-4 border-b border-white/10 pb-6">
      <div class="space-y-1.5">
        <div class="inline-flex items-center space-x-2 px-3 py-1 rounded-full bg-amber-400/10 border border-amber-400/25 text-amber-300 text-xs font-semibold">
          <Disc3 class="w-3.5 h-3.5 animate-spin-slow" />
          <span>星穹列车 · 极客留声机</span>
        </div>
        <h1 class="text-3xl font-extrabold tracking-tight text-slate-800 dark:text-white flex items-center space-x-3">
          <span>星穹音乐厅</span>
          <span class="text-xs font-mono text-slate-500 dark:text-slate-400 font-normal px-2.5 py-1 rounded-lg bg-pink-100/60 dark:bg-white/5 border border-pink-200/60 dark:border-white/5">
            Phonograph
          </span>
        </h1>
        <p class="text-xs text-slate-600 dark:text-slate-400 font-mono">
          愿此行，终抵群星 · 正版原声带高品质沉浸漫游
        </p>
      </div>

      <!-- 右侧快捷模式切换标签 -->
      <div class="flex items-center p-1 rounded-2xl bg-space-900/80 border border-white/10 text-xs select-none">
        <button
          @click="activeTab = 'lyrics'"
          class="px-4 py-2 rounded-xl transition-all font-semibold flex items-center space-x-1.5"
          :class="activeTab === 'lyrics' ? 'bg-gradient-to-r from-amber-400 to-rose-500 text-white shadow-lg shadow-amber-500/20' : 'text-slate-400 hover:text-white'"
        >
          <Mic2 class="w-3.5 h-3.5" />
          <span>动态歌词</span>
        </button>
        <button
          @click="activeTab = 'playlist'"
          class="px-4 py-2 rounded-xl transition-all font-semibold flex items-center space-x-1.5"
          :class="activeTab === 'playlist' ? 'bg-gradient-to-r from-amber-400 to-rose-500 text-white shadow-lg shadow-amber-500/20' : 'text-slate-400 hover:text-white'"
        >
          <ListMusic class="w-3.5 h-3.5" />
          <span>星轨歌单 ({{ musicStore.playlist.length }})</span>
        </button>
      </div>
    </div>

    <!-- 核心主体双栏布局 -->
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
      <!-- 左栏：黑胶唱机拟态与控制面板 (5 Cols) -->
      <div class="lg:col-span-5 glass-island rounded-3xl p-6 sm:p-8 border border-white/15 space-y-6 relative overflow-hidden shadow-2xl">
        <!-- 黑胶唱机拟态 -->
        <div class="relative flex items-center justify-center py-4">
          <!-- 唱机底盘 -->
          <div class="relative w-64 h-64 sm:w-72 sm:h-72 rounded-full bg-[#0a0d14] p-3 shadow-[0_0_50px_rgba(0,0,0,0.8)] border border-white/10 flex items-center justify-center">
            <!-- 黑胶凹槽同心圆环 -->
            <div
              class="w-full h-full rounded-full bg-[radial-gradient(#151b28_1px,transparent_1px)] [background-size:6px_6px] p-2 flex items-center justify-center border border-white/5 relative"
              :class="{ 'animate-spin-slow': musicStore.isPlaying }"
              :style="{ animationPlayState: musicStore.isPlaying ? 'running' : 'paused' }"
            >
              <!-- 黑胶微光同心圆条纹 -->
              <div class="absolute inset-4 rounded-full border border-white/[0.04]"></div>
              <div class="absolute inset-10 rounded-full border border-white/[0.06]"></div>
              <div class="absolute inset-16 rounded-full border border-white/[0.08]"></div>

              <!-- 唱片中心封面 -->
              <div class="w-28 h-28 sm:w-32 sm:h-32 rounded-full overflow-hidden border-4 border-[#07090e] shadow-2xl relative">
                <img
                  :src="musicStore.currentSong.cover"
                  :alt="musicStore.currentSong.title"
                  class="w-full h-full object-cover"
                />
                <!-- 中心金属轴孔 -->
                <div class="absolute inset-0 m-auto w-6 h-6 rounded-full bg-space-950 border-2 border-white/40 flex items-center justify-center shadow-inner">
                  <div class="w-2 h-2 rounded-full bg-white/80"></div>
                </div>
              </div>
            </div>

            <!-- 唱针机械臂拟态 (Turntable Stylus) -->
            <div
              class="absolute -top-2 right-4 w-20 h-28 pointer-events-none origin-top-right transition-transform duration-700 ease-out z-10"
              :class="musicStore.isPlaying ? 'rotate-[24deg]' : 'rotate-0'"
            >
              <div class="w-3.5 h-3.5 rounded-full bg-slate-400 border border-white/40 shadow absolute -top-1 right-0"></div>
              <div class="w-1.5 h-20 bg-gradient-to-b from-slate-400 to-slate-600 rounded-full absolute top-2 right-1.5 shadow"></div>
              <div class="w-4 h-6 rounded bg-amber-400/90 border border-amber-300 absolute bottom-0 -left-1 shadow-lg transform -rotate-12"></div>
            </div>
          </div>
        </div>

        <!-- 歌曲信息 -->
        <div class="text-center space-y-1.5">
          <span class="inline-block px-2.5 py-0.5 rounded-full text-[10px] font-mono bg-nebula-cyan/10 text-nebula-cyan border border-nebula-cyan/20">
            {{ musicStore.currentSong.theme || '星际原声带' }}
          </span>
          <h2 class="text-xl font-bold text-white tracking-wide truncate">
            {{ musicStore.currentSong.title }}
          </h2>
          <p class="text-xs text-slate-400 truncate font-mono">
            {{ musicStore.currentSong.artist }}
          </p>
        </div>

        <!-- 进度条与时间 -->
        <div class="space-y-1.5">
          <div
            class="relative h-2 rounded-full bg-white/10 overflow-hidden cursor-pointer group"
            @click="handleProgressClick"
          >
            <div
              class="h-full bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan transition-all duration-100 rounded-full relative"
              :style="{ width: `${progressPercent}%` }"
            >
              <span class="absolute right-0 top-1/2 -translate-y-1/2 w-3 h-3 rounded-full bg-white shadow-md opacity-0 group-hover:opacity-100 transition-opacity"></span>
            </div>
          </div>
          <div class="flex items-center justify-between text-[11px] font-mono text-slate-400">
            <span>{{ formatTime(musicStore.currentTime) }}</span>
            <span>{{ formatTime(musicStore.duration) }}</span>
          </div>
        </div>

        <!-- 控制按钮集群 -->
        <div class="flex items-center justify-between pt-2">
          <!-- 循环模式切换 -->
          <button
            @click="musicStore.togglePlayMode"
            class="p-2.5 rounded-xl text-slate-400 hover:text-amber-400 hover:bg-white/5 transition-all"
            :title="playModeTitle"
          >
            <Repeat1 v-if="musicStore.playMode === 'single'" class="w-5 h-5 text-amber-400" />
            <Shuffle v-else-if="musicStore.playMode === 'random'" class="w-5 h-5 text-amber-400" />
            <Repeat v-else class="w-5 h-5" />
          </button>

          <!-- 上一首 -->
          <button
            @click="musicStore.prev"
            class="p-3 rounded-2xl text-slate-300 hover:text-white hover:bg-white/10 active:scale-95 transition-all"
            title="上一首"
          >
            <SkipBack class="w-6 h-6" />
          </button>

          <!-- 播放/暂停大按键 -->
          <button
            @click="musicStore.togglePlay"
            class="w-14 h-14 rounded-full bg-gradient-to-tr from-amber-400 via-rose-500 to-nebula-cyan text-white flex items-center justify-center shadow-xl shadow-amber-500/30 hover:scale-105 active:scale-95 transition-all"
            :title="musicStore.isPlaying ? '暂停' : '播放'"
          >
            <Pause v-if="musicStore.isPlaying" class="w-6 h-6 fill-white" />
            <Play v-else class="w-6 h-6 fill-white ml-0.5" />
          </button>

          <!-- 下一首 -->
          <button
            @click="musicStore.next"
            class="p-3 rounded-2xl text-slate-300 hover:text-white hover:bg-white/10 active:scale-95 transition-all"
            title="下一首"
          >
            <SkipForward class="w-6 h-6" />
          </button>

          <!-- 静音与音量 -->
          <button
            @click="musicStore.toggleMute"
            class="p-2.5 rounded-xl text-slate-400 hover:text-white hover:bg-white/5 transition-all"
            :title="musicStore.isMuted ? '取消静音' : '静音'"
          >
            <VolumeX v-if="musicStore.isMuted" class="w-5 h-5 text-rose-400" />
            <Volume2 v-else class="w-5 h-5" />
          </button>
        </div>

        <!-- 音量滑块与律动音波小条 -->
        <div class="flex items-center space-x-3 pt-1 border-t border-white/5">
          <span class="text-[10px] font-mono text-slate-500 uppercase tracking-wider">Vol</span>
          <input
            type="range"
            min="0"
            max="1"
            step="0.01"
            :value="musicStore.volume"
            @input="handleVolumeChange"
            class="flex-1 h-1 bg-white/10 rounded-lg appearance-none cursor-pointer accent-amber-400"
          />
          <!-- 律动音柱 -->
          <div class="flex items-end space-x-1 h-3 shrink-0">
            <span
              v-for="i in 5"
              :key="i"
              class="w-1 bg-nebula-cyan rounded-full transition-all duration-200"
              :style="{
                height: musicStore.isPlaying ? `${Math.max(20, (Math.sin(musicStore.currentTime * 5 + i) + 1) * 50)}%` : '20%',
                opacity: musicStore.isPlaying ? 1 : 0.3
              }"
            ></span>
          </div>
        </div>
      </div>

      <!-- 右栏：动态歌词 或 歌单列表 (7 Cols) -->
      <div class="lg:col-span-7 glass-island rounded-3xl p-6 sm:p-8 border border-white/15 h-[620px] flex flex-col relative overflow-hidden shadow-2xl">
        <!-- 选项卡 1：实时滚动歌词 -->
        <div v-show="activeTab === 'lyrics'" class="flex-1 flex flex-col h-full">
          <!-- 歌词容器标题 -->
          <div class="flex items-center justify-between pb-3 border-b border-white/5 text-xs text-slate-400 font-mono">
            <span class="flex items-center space-x-1.5">
              <Sparkles class="w-3.5 h-3.5 text-amber-400" />
              <span>实时同步唱词 · 点击任意行跳转播放</span>
            </span>
            <span>{{ parsedLyrics.length }} 行</span>
          </div>

          <!-- 歌词滚屏视口 -->
          <div
            ref="lyricsContainerRef"
            class="flex-1 overflow-y-auto py-32 space-y-6 scroll-smooth text-center select-none"
          >
            <!-- 纯音乐特殊占位 -->
            <div v-if="parsedLyrics.length <= 6 && musicStore.currentSong.id === '5'" class="py-16 space-y-4">
              <div class="w-16 h-16 mx-auto rounded-full bg-nebula-cyan/10 border border-nebula-cyan/30 flex items-center justify-center text-nebula-cyan animate-pulse">
                <Music class="w-8 h-8" />
              </div>
              <p class="text-base text-slate-200 font-medium tracking-wide">
                ✦ 纯音乐 · 请享受星际漫步的静谧节奏 ✦
              </p>
              <p class="text-xs text-slate-500 font-mono">
                观测星云流动，记录引力波纹，愿此行，终抵群星
              </p>
            </div>

            <!-- 标准 LRC 歌词行 -->
            <div
              v-for="(line, idx) in parsedLyrics"
              :key="idx"
              :ref="el => setLyricLineRef(el, idx)"
              @click="musicStore.seek(line.time)"
              class="cursor-pointer transition-all duration-300 py-1 px-4 rounded-xl group"
              :class="[
                activeLyricIndex === idx
                  ? 'text-lg sm:text-xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-amber-300 via-rose-300 to-nebula-cyan scale-105 shadow-sm'
                  : 'text-sm text-slate-400 hover:text-slate-200 hover:bg-white/5'
              ]"
            >
              <p class="leading-relaxed tracking-wide">
                {{ line.text }}
              </p>
            </div>
          </div>
        </div>

        <!-- 选项卡 2：星轨留声机歌单列表 -->
        <div v-show="activeTab === 'playlist'" class="flex-1 flex flex-col h-full space-y-4">
          <div class="flex items-center justify-between pb-3 border-b border-white/5 text-xs text-slate-400 font-mono">
            <span>星穹列车 · 经典黑胶唱片列表</span>
            <span>共 {{ musicStore.playlist.length }} 首</span>
          </div>

          <div class="flex-1 overflow-y-auto space-y-2.5 pr-1">
            <div
              v-for="(song, idx) in musicStore.playlist"
              :key="song.id"
              @click="musicStore.switchSong(idx)"
              class="p-3.5 rounded-2xl border transition-all cursor-pointer flex items-center justify-between group"
              :class="[
                musicStore.currentIndex === idx
                  ? 'bg-white/10 border-amber-400/40 shadow-lg shadow-amber-500/10'
                  : 'bg-space-950/60 border-white/5 hover:border-white/15 hover:bg-white/5'
              ]"
            >
              <div class="flex items-center space-x-3.5 min-w-0">
                <!-- 序号 / 播放动画 -->
                <div class="w-8 text-center font-mono text-xs text-slate-500 shrink-0">
                  <div v-if="musicStore.currentIndex === idx && musicStore.isPlaying" class="flex items-end justify-center space-x-0.5 h-3">
                    <span class="w-0.5 h-3 bg-amber-400 animate-pulse"></span>
                    <span class="w-0.5 h-2 bg-amber-400 animate-pulse delay-75"></span>
                    <span class="w-0.5 h-3.5 bg-amber-400 animate-pulse delay-150"></span>
                  </div>
                  <span v-else>{{ idx + 1 }}</span>
                </div>

                <!-- 封面图 -->
                <div class="w-12 h-12 rounded-xl overflow-hidden border border-white/10 shrink-0 relative">
                  <img :src="song.cover" :alt="song.title" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300" />
                  <div
                    v-if="musicStore.currentIndex === idx"
                    class="absolute inset-0 bg-space-950/40 flex items-center justify-center text-amber-400"
                  >
                    <Disc3 class="w-5 h-5 animate-spin-slow" />
                  </div>
                </div>

                <!-- 标题与歌手 -->
                <div class="min-w-0">
                  <h4
                    class="text-sm font-bold truncate transition-colors"
                    :class="musicStore.currentIndex === idx ? 'text-amber-300' : 'text-slate-100 group-hover:text-white'"
                  >
                    {{ song.title }}
                  </h4>
                  <p class="text-xs text-slate-400 truncate mt-0.5 font-mono">
                    {{ song.artist }}
                  </p>
                </div>
              </div>

              <!-- 右侧主题标签与操作 -->
              <div class="flex items-center space-x-3 shrink-0">
                <span class="hidden sm:inline px-2 py-0.5 rounded text-[10px] font-mono bg-white/5 text-slate-400 border border-white/5">
                  {{ song.theme }}
                </span>
                <button
                  class="p-2 rounded-xl text-slate-400 hover:text-white hover:bg-white/10 transition-colors"
                  :title="musicStore.currentIndex === idx && musicStore.isPlaying ? '正在播放' : '点击播放'"
                >
                  <Play v-if="!(musicStore.currentIndex === idx && musicStore.isPlaying)" class="w-4 h-4" />
                  <Pause v-else class="w-4 h-4 text-amber-400 fill-amber-400" />
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue';
import {
  Disc3,
  Mic2,
  ListMusic,
  Play,
  Pause,
  SkipBack,
  SkipForward,
  Repeat,
  Repeat1,
  Shuffle,
  Volume2,
  VolumeX,
  Sparkles,
  Music
} from 'lucide-vue-next';
import { useMusicStore } from '@/stores/music';

const musicStore = useMusicStore();
const activeTab = ref<'lyrics' | 'playlist'>('lyrics');

const lyricsContainerRef = ref<HTMLElement | null>(null);
const lyricLineRefs = ref<(HTMLElement | null)[]>([]);

const setLyricLineRef = (el: any, index: number) => {
  lyricLineRefs.value[index] = el;
};

// 解析当前歌曲的 LRC 歌词
const parsedLyrics = computed(() => {
  const rawLrc = musicStore.currentSong.lyrics || '';
  if (!rawLrc) return [];

  const lines = rawLrc.split('\n');
  const result: { time: number; text: string }[] = [];
  const timeRegex = /\[(\d{2}):(\d{2})(?:\.(\d{2,3}))?\]/;

  for (const line of lines) {
    const match = timeRegex.exec(line);
    if (match) {
      const minutes = parseInt(match[1], 10);
      const seconds = parseInt(match[2], 10);
      const milliseconds = match[3] ? parseInt(match[3].padEnd(3, '0').slice(0, 3), 10) : 0;
      const time = minutes * 60 + seconds + milliseconds / 1000;
      const text = line.replace(timeRegex, '').trim();
      if (text) {
        result.push({ time, text });
      }
    }
  }

  return result.sort((a, b) => a.time - b.time);
});

// 计算当前正在演唱的歌词行索引
const activeLyricIndex = computed(() => {
  const current = musicStore.currentTime;
  const list = parsedLyrics.value;
  if (!list.length) return -1;

  for (let i = list.length - 1; i >= 0; i--) {
    if (current >= list[i].time) {
      return i;
    }
  }
  return 0;
});

// 监听当前歌词高亮变化，平滑自动居中滚动
watch(activeLyricIndex, async (newIdx) => {
  if (activeTab.value !== 'lyrics' || newIdx < 0) return;
  await nextTick();
  const el = lyricLineRefs.value[newIdx];
  const container = lyricsContainerRef.value;
  if (el && container) {
    const topPos = el.offsetTop - container.offsetTop - container.clientHeight / 2 + el.clientHeight / 2;
    container.scrollTo({
      top: topPos,
      behavior: 'smooth'
    });
  }
});

// 计算进度百分比
const progressPercent = computed(() => {
  if (!musicStore.duration) return 0;
  return Math.min(100, (musicStore.currentTime / musicStore.duration) * 100);
});

// 格式化秒数为 mm:ss
const formatTime = (secs: number) => {
  if (isNaN(secs) || secs <= 0) return '00:00';
  const m = Math.floor(secs / 60);
  const s = Math.floor(secs % 60);
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`;
};

// 点击进度条调整播放进度
const handleProgressClick = (e: MouseEvent) => {
  const el = e.currentTarget as HTMLElement;
  const rect = el.getBoundingClientRect();
  const clickX = e.clientX - rect.left;
  const percent = Math.max(0, Math.min(1, clickX / rect.width));
  const targetTime = percent * musicStore.duration;
  musicStore.seek(targetTime);
};

// 调节音量
const handleVolumeChange = (e: Event) => {
  const target = e.target as HTMLInputElement;
  musicStore.setVolume(parseFloat(target.value));
};

const playModeTitle = computed(() => {
  if (musicStore.playMode === 'single') return '单曲循环';
  if (musicStore.playMode === 'random') return '随机播放';
  return '列表循环';
});
</script>

<style scoped>
/* 滚动条隐藏与高颜值微光 */
::-webkit-scrollbar {
  width: 4px;
}
::-webkit-scrollbar-track {
  background: transparent;
}
::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 9999px;
}
::-webkit-scrollbar-thumb:hover {
  background: rgba(255, 255, 255, 0.25);
}
</style>
