<template>
  <div class="max-w-6xl mx-auto px-4 sm:px-6 pt-6 pb-20 space-y-6">
    <!-- 顶部全局星际搜索栏 (Search Capsule) -->
    <div class="max-w-2xl mx-auto">
      <div class="relative group">
        <div class="absolute -inset-0.5 bg-gradient-to-r from-nebula-cyan/30 via-nebula-purple/30 to-nebula-pink/30 rounded-2xl blur opacity-40 group-hover:opacity-100 transition duration-500"></div>
        <div class="relative flex items-center glass-island rounded-2xl px-5 py-3 border border-white/15">
          <Search class="w-5 h-5 text-nebula-cyan mr-3 shrink-0" />
          <input
            v-model="searchKeyword"
            @keyup.enter="handleSearch"
            type="text"
            placeholder="搜寻星向知识库、文章标题、标签或灵感..."
            class="bg-transparent border-none outline-none text-sm text-slate-100 placeholder-slate-400 w-full"
          />
          <button
            @click="handleSearch"
            class="text-xs font-mono text-slate-400 hover:text-white px-2 py-1 rounded bg-white/5 border border-white/10"
          >
            Enter ↵
          </button>
        </div>
      </div>
    </div>

    <!-- 便当盒第一排 (Bento Row 1): 个人开拓者档案 + 留声机音乐播放器 -->
    <div class="grid grid-cols-1 lg:grid-cols-12 gap-6">
      <!-- 左侧：个人开拓档案卡 (7 列) -->
      <div class="lg:col-span-7 glass-card rounded-3xl p-6 sm:p-7 border border-white/10 flex flex-col justify-between relative overflow-hidden group">
        <!-- 背景微光遮罩 -->
        <div class="absolute -right-10 -bottom-10 w-44 h-44 bg-nebula-cyan/10 rounded-full blur-3xl pointer-events-none"></div>

        <div class="space-y-4">
          <!-- 顶部头像与信息 -->
          <div class="flex items-start space-x-5">
            <div class="relative group cursor-pointer" @click="$router.push('/about')">
              <div class="w-20 h-20 sm:w-22 sm:h-22 rounded-2xl p-[2px] bg-gradient-to-tr from-nebula-cyan via-nebula-purple to-nebula-pink shadow-xl group-hover:scale-105 transition-transform">
                <img
                  src="https://github.com/NightMare33133.png"
                  alt="NightMare33133"
                  class="w-full h-full object-cover rounded-[14px]"
                />
              </div>
              <div class="absolute -bottom-1 -right-1 w-4 h-4 rounded-full bg-space-950 flex items-center justify-center">
                <div class="w-2.5 h-2.5 rounded-full bg-emerald-400 animate-pulse"></div>
              </div>
            </div>

            <div class="flex-1 min-w-0">
              <div class="flex items-center space-x-2">
                <h2 class="text-xl sm:text-2xl font-extrabold text-white tracking-wide truncate">NightMare33133</h2>
                <span class="px-2 py-0.5 rounded-full text-[10px] font-mono bg-nebula-cyan/10 text-nebula-cyan border border-nebula-cyan/20">
                  开拓者
                </span>
              </div>
              <p class="text-xs text-slate-300 mt-1.5 leading-relaxed line-clamp-2">
                在代码、系统与探索间穿梭。行进于复旦与香港城大之间，记录每一次星芒跃迁。
              </p>
            </div>
          </div>

          <!-- 社交图标与指标 -->
          <div class="pt-2 flex flex-wrap items-center justify-between gap-4 border-t border-white/5">
            <!-- 核心数据指标 -->
            <div class="flex items-center space-x-6 text-center">
              <div>
                <div class="text-lg font-bold font-mono text-white">{{ posts.length }}</div>
                <div class="text-[10px] text-slate-400">文章</div>
              </div>
              <div>
                <div class="text-lg font-bold font-mono text-nebula-cyan">{{ moments.length }}</div>
                <div class="text-[10px] text-slate-400">碎语</div>
              </div>
              <div>
                <div class="text-lg font-bold font-mono text-nebula-pink">6</div>
                <div class="text-[10px] text-slate-400">照片</div>
              </div>
            </div>

            <!-- 社交链接组 -->
            <div class="flex items-center space-x-2">
              <a
                href="https://github.com/NightMare33133"
                target="_blank"
                class="w-8 h-8 rounded-full glass-card hover:bg-white/10 flex items-center justify-center text-slate-300 hover:text-white transition-all"
                title="GitHub"
              >
                <Github class="w-4 h-4" />
              </a>
              <router-link
                to="/about"
                class="w-8 h-8 rounded-full glass-card hover:bg-white/10 flex items-center justify-center text-slate-300 hover:text-nebula-cyan transition-all"
                title="关于我"
              >
                <User class="w-4 h-4" />
              </router-link>
              <router-link
                to="/moments"
                class="w-8 h-8 rounded-full glass-card hover:bg-white/10 flex items-center justify-center text-slate-300 hover:text-nebula-pink transition-all"
                title="星际碎语"
              >
                <Sparkles class="w-4 h-4" />
              </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：星穹列车留声机播放器卡片 (5 列) -->
      <div class="lg:col-span-5 glass-card rounded-3xl p-6 border border-white/10 relative overflow-hidden flex flex-col justify-between">
        <!-- 留声机光效 -->
        <div class="absolute -right-8 -top-8 w-32 h-32 rounded-full blur-2xl opacity-20 bg-nebula-purple pointer-events-none"></div>

        <div class="space-y-4">
          <!-- 顶栏标签 -->
          <div class="flex items-center justify-between">
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-mono tracking-wider bg-nebula-purple/20 text-nebula-purple border border-nebula-purple/30 uppercase font-bold">
              ✦ ASTRAL PHONOGRAPH
            </span>
            <span class="text-[10px] font-mono text-slate-400">列车留声机</span>
          </div>

          <!-- 黑胶唱片与歌曲信息 -->
          <div class="flex items-center space-x-4">
            <!-- 旋转黑胶 -->
            <div class="relative shrink-0 cursor-pointer" @click="musicStore.togglePlay">
              <div
                class="w-16 h-16 rounded-full overflow-hidden border-2 border-white/20 shadow-xl"
                :class="{ 'animate-spin-slow': musicStore.isPlaying }"
                :style="{ animationPlayState: musicStore.isPlaying ? 'running' : 'paused' }"
              >
                <img :src="musicStore.currentSong.cover" :alt="musicStore.currentSong.title" class="w-full h-full object-cover" />
              </div>
              <div class="absolute inset-0 m-auto w-4 h-4 rounded-full bg-space-950 border border-white/30 flex items-center justify-center">
                <div class="w-1.5 h-1.5 rounded-full bg-white/70"></div>
              </div>
            </div>

            <div class="flex-1 min-w-0">
              <h3 class="text-sm font-bold text-white truncate">{{ musicStore.currentSong.title }}</h3>
              <p class="text-xs text-slate-400 truncate">{{ musicStore.currentSong.artist }}</p>
              
              <!-- 律动音波 -->
              <div class="flex items-end space-x-1 h-3 mt-1.5">
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
        </div>

        <!-- 进度条与播放控制 -->
        <div class="space-y-2 mt-4 pt-3 border-t border-white/5">
          <div
            class="h-1.5 w-full bg-white/10 rounded-full overflow-hidden cursor-pointer"
            @click="handleSeek"
            ref="progressTrackRef"
          >
            <div
              class="h-full bg-gradient-to-r from-nebula-cyan to-nebula-purple rounded-full"
              :style="{ width: `${progressPercent}%` }"
            ></div>
          </div>
          <div class="flex items-center justify-between text-[10px] font-mono text-slate-500">
            <span>{{ formatTime(musicStore.currentTime) }}</span>
            <div class="flex items-center space-x-4 text-slate-300">
              <button @click="musicStore.prev" class="hover:text-white transition-colors">
                <SkipBack class="w-3.5 h-3.5" />
              </button>
              <button
                @click="musicStore.togglePlay"
                class="w-7 h-7 rounded-full bg-nebula-purple/30 hover:bg-nebula-purple/50 text-white flex items-center justify-center transition-all"
              >
                <Play v-if="!musicStore.isPlaying" class="w-3 h-3 ml-0.5" />
                <Pause v-else class="w-3 h-3" />
              </button>
              <button @click="musicStore.next" class="hover:text-white transition-colors">
                <SkipForward class="w-3.5 h-3.5" />
              </button>
            </div>
            <span>{{ formatTime(musicStore.duration) }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 便当盒第二排 (Bento Row 2): 漫游电台跑马灯胶囊 (Cosmic Ticker) -->
    <div class="glass-card rounded-2xl py-3 px-6 border border-white/10 flex items-center justify-between text-xs font-mono text-slate-300 relative overflow-hidden">
      <div class="flex items-center space-x-3 truncate">
        <span class="text-nebula-cyan animate-pulse font-bold">✦</span>
        <span class="text-slate-400">漫游电台：</span>
        <span class="text-white font-medium tracking-wide">
          「愿此行，终抵群星」—— 星穹列车广播已接入 · 前往下一个未知的星系探索 🚀
        </span>
      </div>
      <div class="hidden sm:flex items-center space-x-2 text-[10px] text-slate-500 shrink-0 pl-4 font-mono">
        <span>TRANSMISSION ACTIVE</span>
        <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-ping"></span>
      </div>
    </div>

    <!-- 便当盒第三排 (Bento Row 3): 重点文章大图卡 + 摄影卡 + 碎语卡 + 跃迁卡 -->
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-12 gap-6">
      <!-- 卡片 1 (左侧大卡，占 5 列): LATEST INSIGHT 重点推荐文章 -->
      <div
        class="lg:col-span-5 rounded-3xl overflow-hidden relative group cursor-pointer border border-white/10 hover:border-nebula-cyan/40 transition-all duration-500 min-h-[340px] flex flex-col justify-end p-7"
        @click="posts.length > 0 && $router.push(`/posts/${posts[0].id}`)"
      >
        <!-- 背景大图：姬子与星穹列车 (银河铁道之夜) -->
        <img
          src="/images/hsr/himeko_express.png"
          alt="Featured Article"
          class="absolute inset-0 w-full h-full object-cover object-center group-hover:scale-105 transition-transform duration-700"
        />
        <!-- 暗调渐变蒙版保证文字极致可读 -->
        <div class="absolute inset-0 bg-gradient-to-t from-space-950 via-space-950/60 to-transparent"></div>

        <!-- 内容 -->
        <div class="relative z-10 space-y-3">
          <div class="flex items-center space-x-2">
            <span class="px-2.5 py-0.5 rounded-full text-[10px] font-bold font-mono bg-nebula-cyan text-space-950 uppercase tracking-wider">
              LATEST INSIGHT
            </span>
            <span v-if="posts.length > 0" class="text-xs font-mono text-slate-300">
              {{ formatDate(posts[0].createdAt || posts[0].publishedAt) }}
            </span>
          </div>

          <h3 class="text-xl font-bold text-white group-hover:text-nebula-cyan transition-colors leading-tight">
            {{ posts.length > 0 ? posts[0].title : '你好，星向空间 (Hello Starward Space)' }}
          </h3>

          <p class="text-xs text-slate-300 line-clamp-2 leading-relaxed">
            {{ posts.length > 0 ? posts[0].summary : '并不是为了向世界证明什么，而是在嘈杂的信息洪流与内卷浪潮里，亲手为自己搭建一个有温度、有审美的赛博自留地。' }}
          </p>

          <!-- 标签 -->
          <div class="flex items-center gap-1.5 pt-1">
            <span
              v-for="t in (posts.length > 0 ? posts[0].tags : [])"
              :key="t.id"
              class="px-2 py-0.5 rounded text-[10px] bg-white/10 text-slate-200 backdrop-blur-sm"
            >
              # {{ t.name }}
            </span>
          </div>
        </div>
      </div>

      <!-- 右侧四宫格区块 (占 7 列) -->
      <div class="lg:col-span-7 grid grid-cols-1 sm:grid-cols-2 gap-6">
        <!-- 卡片 2 (摄影视界大卡) -->
        <div
          class="sm:col-span-2 rounded-3xl overflow-hidden relative group cursor-pointer border border-white/10 hover:border-nebula-pink/40 transition-all duration-500 min-h-[170px] flex flex-col justify-end p-6"
          @click="$router.push('/gallery')"
        >
          <!-- 背景图：三月七自拍 -->
          <img
            src="/images/hsr/march7th_selfie.png"
            alt="Photography Gallery"
            class="absolute inset-0 w-full h-full object-cover object-top group-hover:scale-105 transition-transform duration-700"
          />
          <div class="absolute inset-0 bg-gradient-to-t from-space-950 via-space-950/50 to-transparent"></div>

          <div class="relative z-10 space-y-1">
            <span class="px-2 py-0.5 rounded text-[9px] font-mono bg-nebula-pink/20 text-nebula-pink border border-nebula-pink/30 uppercase">
              VISUAL ODYSSEY · 摄影视界
            </span>
            <h4 class="text-base font-bold text-white group-hover:text-nebula-pink transition-colors">
              列车组日常与现实光影漫游
            </h4>
            <p class="text-xs text-slate-300">代码之外，用镜头捕获光影流转与温柔角落 ➔</p>
          </div>
        </div>

        <!-- 卡片 3 (星际碎语模组卡) -->
        <div
          class="glass-card rounded-3xl p-5 border border-white/10 space-y-3 cursor-pointer hover:border-nebula-purple/40 transition-all flex flex-col justify-between"
          @click="$router.push('/moments')"
        >
          <div class="flex items-center justify-between">
            <span class="text-xs font-mono text-nebula-purple font-bold">MOMENTS · 碎语</span>
            <span class="text-base">{{ moments.length > 0 ? (moments[0].moodEmoji || '✨') : '✨' }}</span>
          </div>

          <p class="text-xs text-slate-200 leading-relaxed line-clamp-3">
            {{ moments.length > 0 ? moments[0].content : '正在用 Vue 3 与 Spring Boot 3 打造属于自己的星向空间！🚀' }}
          </p>

          <div class="text-[10px] text-slate-500 font-mono flex items-center justify-between pt-2 border-t border-white/5">
            <span>{{ moments.length > 0 ? formatDate(moments[0].createdAt) : '2026.09' }}</span>
            <span class="text-nebula-purple">发射电波 ➔</span>
          </div>
        </div>

        <!-- 卡片 4 (跃迁模式/深空控制卡) -->
        <div class="glass-card rounded-3xl p-5 border border-white/10 space-y-3 flex flex-col justify-between text-center relative overflow-hidden">
          <div class="w-10 h-10 mx-auto rounded-2xl bg-gradient-to-tr from-amber-400 to-nebula-pink p-[1px] shadow-lg">
            <div class="w-full h-full bg-space-950 rounded-[15px] flex items-center justify-center">
              <Sparkles class="w-5 h-5 text-amber-400 animate-pulse" />
            </div>
          </div>

          <div>
            <h4 class="text-sm font-bold text-white">跃迁漫游模式</h4>
            <p class="text-[11px] text-slate-400 mt-1">金色流萤 飞舞的星轨深空</p>
          </div>

          <div class="flex items-center justify-center space-x-1 text-[10px] font-mono text-emerald-400">
            <span class="w-1.5 h-1.5 rounded-full bg-emerald-400 animate-ping"></span>
            <span>SYSTEM OPTIMAL</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 便当盒第四排 (Bento Row 4): 实时数字时钟 + 系统运行时间 + 技术栈与米哈游声明 -->
    <div class="glass-island rounded-2xl p-4 sm:p-5 border border-white/10 flex flex-col md:flex-row items-center justify-between gap-4">
      <!-- 实时 OLED 动态数字时钟 -->
      <div class="flex items-center space-x-4">
        <div class="px-4 py-2 rounded-xl bg-black border border-white/15 text-nebula-cyan font-mono text-xl sm:text-2xl font-bold tracking-widest shadow-inner shadow-nebula-cyan/20">
          {{ currentTimeStr }}
        </div>
        <div class="space-y-0.5">
          <div class="flex items-center space-x-1.5 text-xs text-slate-300 font-mono">
            <span class="w-2 h-2 rounded-full bg-emerald-400 animate-pulse"></span>
            <span>系统已稳定运行：<span class="text-nebula-cyan font-bold">{{ runningDays }}</span> 天</span>
          </div>
          <p class="text-[10px] text-slate-500 font-mono">Astral Express Node: #01-EAST-ASIA</p>
        </div>
      </div>

      <!-- 技术栈与版权说明 -->
      <div class="flex flex-col items-center md:items-end space-y-1 text-center md:text-right">
        <div class="flex flex-wrap items-center justify-center gap-1.5">
          <span v-for="t in ['Vue 3.5', 'Vite 8', 'Tailwind', 'Spring Boot 3', 'Java 21 LTS']" :key="t" class="px-2 py-0.5 rounded text-[10px] font-mono bg-white/5 text-slate-400 border border-white/5">
            {{ t }}
          </span>
        </div>
        <p class="text-[10px] text-slate-600 font-mono">
          All Star Rail visual assets belong to miHoYo / HoYoverse · Non-commercial personal blog
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { Search, Github, User, Sparkles, SkipBack, SkipForward, Play, Pause } from 'lucide-vue-next';
import { getPostList } from '@/api/posts';
import { getMoments } from '@/api/moments';
import { useMusicStore } from '@/stores/music';
import type { PostListVO, Moment } from '@/types';

const router = useRouter();
const musicStore = useMusicStore();

const posts = ref<PostListVO[]>([]);
const moments = ref<Moment[]>([]);
const searchKeyword = ref('');
const progressTrackRef = ref<HTMLElement | null>(null);

// 实时时钟
const currentTimeStr = ref('');
let timerId: number;

const updateClock = () => {
  const now = new Date();
  const h = now.getHours().toString().padStart(2, '0');
  const m = now.getMinutes().toString().padStart(2, '0');
  const s = now.getSeconds().toString().padStart(2, '0');
  currentTimeStr.value = `${h}:${m}:${s}`;
};

// 稳定运行天数
const startDate = new Date('2024-09-01').getTime();
const runningDays = computed(() => {
  const diff = Date.now() - startDate;
  return Math.max(1, Math.floor(diff / (1000 * 60 * 60 * 24)));
});

// 音乐播放进度
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

const formatDate = (dateStr?: string) => {
  if (!dateStr) return '';
  const d = new Date(dateStr);
  return `${d.getFullYear()}.${(d.getMonth() + 1).toString().padStart(2, '0')}.${d.getDate().toString().padStart(2, '0')}`;
};

const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ path: '/posts', query: { q: searchKeyword.value.trim() } });
  } else {
    router.push('/posts');
  }
};

onMounted(async () => {
  updateClock();
  timerId = window.setInterval(updateClock, 1000);

  try {
    const [pList, mList] = await Promise.all([
      getPostList().catch(() => []),
      getMoments().catch(() => [])
    ]);
    posts.value = pList;
    moments.value = mList;
  } catch (err) {
    console.error('Failed to load bento data:', err);
  }
});

onUnmounted(() => {
  clearInterval(timerId);
});
</script>
