import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Song } from '@/types';

export const useMusicStore = defineStore('music', () => {
  const isPlaying = ref(false);
  const isExpanded = ref(false);
  const currentTime = ref(0);
  const duration = ref(0);
  const volume = ref(0.7);
  const isMuted = ref(false);
  const previousVolume = ref(0.7);

  // 播放模式: 'list' (列表循环), 'single' (单曲循环), 'random' (随机播放)
  const playMode = ref<'list' | 'single' | 'random'>('list');

  // 星穹列车留声机精选唱片歌单 (带专属同步 LRC 歌词与二次元专辑封面)
  const playlist = ref<Song[]>([
    {
      id: '1',
      title: 'Take the Journey (踏上旅途)',
      artist: 'Anthony Lynch · HOYO-MiX',
      cover: '/images/hsr/himeko_express.png',
      url: '/audio/take_the_journey.mp3',
      theme: '星穹列车 · 启航曲',
      lyrics: `[00:00.00]Take the Journey (踏上旅途) - Anthony Lynch / HOYO-MiX
[00:02.50]✦ 《崩坏：星穹铁道》星穹列车启航曲 ✦
[00:06.00]Step out into the world
[00:09.60]A journey to begin
[00:13.20]So pack your bags and head on out
[00:16.80]Don't look back, there is no doubt
[00:20.60]The road is long, the sky is high
[00:24.40]We will spread our wings and fly
[00:28.10]Through the stars and through the night
[00:31.70]Chasing every beam of light
[00:35.30]Take the journey, feel the breeze
[00:38.90]Past the mountains and the seas
[00:42.60]Every step a memory made
[00:46.10]In our hearts it will not fade
[00:50.00]Take the journey!
[00:53.20]Into the boundless starry sea...
[00:58.00]✦ 愿此行，终抵群星 ✦`
    },
    {
      id: '2',
      title: '野火 Wildfire (造物引擎)',
      artist: 'Jonathan Steingard · HOYO-MiX',
      cover: '/images/hsr/danheng.png',
      url: '/audio/wildfire.mp3',
      theme: '雅利洛-VI · 可可利亚决战战歌',
      lyrics: `[00:00.00]野火 Wildfire - Jonathan Steingard / HOYO-MiX
[00:04.00]✦ 雅利洛-VI · 可可利亚造物引擎决战 ✦
[00:08.50]We've made a choice, go fight against your fate
[00:12.50]Pain will come with the blade
[00:16.00]Pain will wake up the despondent crowd in this dormant town
[00:23.00]Somehow, I remember
[00:26.50]The warm breeze of that dawn
[00:30.00]Now the cold wind whispers
[00:34.00]That the dawn has long been gone
[00:38.00]We've made a choice, go fight against your fate
[00:42.00]Pain will come with the blade
[00:45.50]Never blade will let you bleed
[00:49.00]We'll defeat the winter's greed
[00:53.00]We will burn it down, make 'em burn, burn, burn!
[01:00.50]Wildfire!
[01:04.50]Ignite the spark within the freezing cold!
[01:11.50]Let the burning fire unfold!
[01:18.50]Wildfire!
[01:25.50]Through the frost and endless snow
[01:29.50]We will let our passions grow
[01:33.50]Stand tall, break the ice away
[01:37.50]Welcome to a brand new day!
[01:41.00]We've made a choice, go fight against your fate
[01:45.00]Pain will come with the blade
[01:48.50]Burn it down, make 'em burn!
[01:52.50]Wildfire!
[02:00.00]✦ 存护与开拓的钢铁意志 ✦`
    },
    {
      id: '3',
      title: '使一颗心免于哀伤',
      artist: '知更鸟 · Chevy · HOYO-MiX',
      cover: '/images/hsr/march7th_selfie.png',
      url: '/audio/robin_heart.mp3',
      theme: '匹诺康尼 · 知更鸟之歌',
      lyrics: `[00:00.00]使一颗心免于哀伤 (If I Can Stop One Heart From Breaking) - 知更鸟 (Chevy) / HOYO-MiX
[00:05.00]✦ 匹诺康尼 · 知更鸟之歌 ✦
[00:10.00]雨停之后 拂晓渐落
[00:18.00]微光抚平了夜的锁
[00:26.00]若我能让 一颗心免于哀伤
[00:34.00]我便没有 虚度此生
[00:42.00]若我能抚慰 哪怕一个人的痛苦
[00:50.00]或者减轻 一丝折磨
[00:58.00]若我能帮助 一只昏厥的知更鸟
[01:06.00]重回它的巢窝
[01:14.00]If I can stop one heart from breaking
[01:22.00]I shall not live in vain
[01:30.00]If I can ease one life the aching
[01:38.00]Or cool one pain
[01:46.00]Or help one fainting robin
[01:54.00]Unto his nest again
[02:02.00]I shall not live in vain
[02:10.00]✦ 歌声在梦境与银河间永恒回荡 ✦`
    },
    {
      id: '4',
      title: '不眠之夜 WHITE NIGHT',
      artist: '张杰 · HOYO-MiX',
      cover: '/images/pompom.png',
      url: '/audio/white_night.mp3',
      theme: '匹诺康尼 · 盛会之星主题歌',
      lyrics: `[00:00.00]不眠之夜 WHITE NIGHT - 张杰 / HOYO-MiX
[00:04.00]✦ 匹诺康尼 · 盛会之星梦境嘉年华 ✦
[00:08.50]倒数声在耳边回响 繁华撕碎了迷茫
[00:12.50]每一秒都是狂欢的开场
[00:16.50]Welcome to my world!
[00:20.50]梦境在霓虹中摇晃 撕开黑夜的伪装
[00:24.50]在不眠的城邦 放肆歌唱
[00:28.50]每一束光 都在发烫
[00:32.50]别去管 昨天的伤
[00:36.50]把时间抛在脑后 跃入这片星芒
[00:40.50]Welcome to my world!
[00:44.50]不眠之夜 狂欢未央
[00:48.50]随节奏心跳 尽情释放
[00:52.50]Welcome to my world!
[00:56.50]这梦境永不散场！
[01:02.00]✦ 欢迎来到梦境之城 匹诺康尼 ✦`
    },
    {
      id: '5',
      title: '太空漫步 Space Walk',
      artist: 'HOYO-MiX',
      cover: '/images/hsr/welt.png',
      url: '/audio/space_walk.mp3',
      theme: '黑塔空间站 · 纯享氛围原声',
      lyrics: `[00:00.00]太空漫步 Space Walk - HOYO-MiX
[00:05.00]✦ 黑塔空间站 · 奇物主控舱段 ✦
[00:12.00]✦ 纯音乐 · 请享受星际漫步的静谧节奏 ✦
[00:25.00]✦ 虚数引擎平稳巡航中 ✦
[00:45.00]✦ 观测星云流动，记录引力波纹 ✦
[01:05.00]✦ 愿此行，终抵群星 ✦`
    }
  ]);

  const currentIndex = ref(0);
  const currentSong = ref<Song>(playlist.value[0]);
  const audioElement = ref<HTMLAudioElement | null>(null);

  const initAudio = () => {
    if (!audioElement.value) {
      audioElement.value = new Audio(currentSong.value.url);
      audioElement.value.volume = isMuted.value ? 0 : volume.value;
      audioElement.value.preload = 'auto';

      audioElement.value.addEventListener('timeupdate', () => {
        if (audioElement.value) {
          currentTime.value = audioElement.value.currentTime;
        }
      });

      audioElement.value.addEventListener('loadedmetadata', () => {
        if (audioElement.value) {
          duration.value = audioElement.value.duration || 0;
        }
      });

      audioElement.value.addEventListener('durationchange', () => {
        if (audioElement.value) {
          duration.value = audioElement.value.duration || 0;
        }
      });

      audioElement.value.addEventListener('ended', () => {
        if (playMode.value === 'single') {
          if (audioElement.value) {
            audioElement.value.currentTime = 0;
            audioElement.value.play().catch(console.warn);
          }
        } else {
          next();
        }
      });

      audioElement.value.addEventListener('error', (e) => {
        console.error('Audio playback error:', e);
        isPlaying.value = false;
      });
    }
  };

  const play = () => {
    initAudio();
    if (audioElement.value) {
      if (!audioElement.value.src.endsWith(currentSong.value.url)) {
        audioElement.value.src = currentSong.value.url;
      }
      audioElement.value.play().then(() => {
        isPlaying.value = true;
      }).catch(err => {
        console.warn('Audio playback failed or blocked:', err);
        isPlaying.value = false;
      });
    }
  };

  const pause = () => {
    if (audioElement.value) {
      audioElement.value.pause();
      isPlaying.value = false;
    }
  };

  const togglePlay = () => {
    if (isPlaying.value) {
      pause();
    } else {
      play();
    }
  };

  const switchSong = (index: number) => {
    currentIndex.value = index;
    currentSong.value = playlist.value[index];
    initAudio();
    if (audioElement.value) {
      audioElement.value.src = currentSong.value.url;
      audioElement.value.currentTime = 0;
      currentTime.value = 0;
      duration.value = 0;
      if (isPlaying.value) {
        audioElement.value.play().catch(err => {
          console.warn('Audio switch play blocked:', err);
          isPlaying.value = false;
        });
      }
    }
  };

  const next = () => {
    if (playMode.value === 'random') {
      let randIdx = Math.floor(Math.random() * playlist.value.length);
      if (randIdx === currentIndex.value && playlist.value.length > 1) {
        randIdx = (randIdx + 1) % playlist.value.length;
      }
      switchSong(randIdx);
      return;
    }
    const nextIdx = (currentIndex.value + 1) % playlist.value.length;
    switchSong(nextIdx);
  };

  const prev = () => {
    if (playMode.value === 'random') {
      let randIdx = Math.floor(Math.random() * playlist.value.length);
      switchSong(randIdx);
      return;
    }
    const prevIdx = (currentIndex.value - 1 + playlist.value.length) % playlist.value.length;
    switchSong(prevIdx);
  };

  const seek = (time: number) => {
    initAudio();
    if (audioElement.value) {
      audioElement.value.currentTime = time;
      currentTime.value = time;
    }
  };

  const setVolume = (val: number) => {
    volume.value = Math.max(0, Math.min(1, val));
    isMuted.value = volume.value === 0;
    if (audioElement.value) {
      audioElement.value.volume = volume.value;
    }
  };

  const toggleMute = () => {
    if (isMuted.value) {
      isMuted.value = false;
      setVolume(previousVolume.value || 0.7);
    } else {
      previousVolume.value = volume.value;
      isMuted.value = true;
      setVolume(0);
    }
  };

  const togglePlayMode = () => {
    if (playMode.value === 'list') {
      playMode.value = 'single';
    } else if (playMode.value === 'single') {
      playMode.value = 'random';
    } else {
      playMode.value = 'list';
    }
  };

  return {
    isPlaying,
    isExpanded,
    currentTime,
    duration,
    volume,
    isMuted,
    playMode,
    playlist,
    currentSong,
    currentIndex,
    play,
    pause,
    togglePlay,
    next,
    prev,
    seek,
    setVolume,
    toggleMute,
    togglePlayMode,
    switchSong,
  };
});
