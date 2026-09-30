import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
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
      cover: '/images/hsr/himeko_express.webp',
      url: '/audio/take_the_journey.mp3',
      theme: '星穹列车 · 启航曲',
      lyrics: `[00:00.00]Take the Journey (踏上旅途) - Anthony Lynch / HOYO-MiX
[00:02.33]The way,
[00:04.98]To celebrate.
[00:08.70]I’ll be waiting
[00:11.62]Till we make it
[00:15.07]The day,
[00:17.99]To celebrate.
[00:21.71]I’ll be waiting
[00:24.65]Till we make it.
[00:28.90]Oh——
[00:35.28]We will see,
[00:38.99]Come with me take the journey.
[00:41.93]Oh——
[00:48.30]We will see,
[00:52.01]Come with me take the journey.
[00:58.00]✦ 愿此行，终抵群星 ✦`
    },
    {
      id: '2',
      title: '野火 Wildfire (造物引擎)',
      artist: 'Jonathan Steingard · HOYO-MiX',
      cover: '/images/hsr/danheng.webp',
      url: '/audio/wildfire.mp3',
      theme: '雅利洛-VI · 可可利亚决战战歌',
      lyrics: `[00:00.00]野火 Wildfire - Jonathan Steingard / HOYO-MiX
[00:03.13]Wrapped in biting wind
[00:06.31]Hearts will never bleed
[00:09.76]Frozen and banished
[00:13.22]Out of grief
[00:16.41]In their restless dreams
[00:19.60]They try so hard to breathe
[00:23.07]Pulses flutter and sting
[00:26.51]Within this bleakness
[00:43.24]Pain will come with the blade
[00:46.42]Pain will wake up the despondent crowd in this dormant world somehow
[00:59.39]Unsheathe a sword not to kill
[01:00.18]Unsheathe a sword to rend those clouds above the ground
[01:04.70]Wake up, it’s time to gather now
[01:09.75]The only warmth remains in hands clasped so tight
[01:16.91]The only fire exists in brave hearts
[01:23.29]Seasons that refuse to change over the years
[01:29.93]Will find their way back, back on track
[01:45.08]We’ve made a choice
[01:45.87]Go fight against your fate!
[01:49.85]Pain will come with the blade
[01:53.04]Pain will wake up the despondent crowd in this dormant world somehow
[02:03.14]Unsheathe a sword not to kill
[02:06.33]Unsheathe a sword to rend those clouds above the ground
[02:11.63]Wake up, it’s time to gather now
[02:24.39]Forget about the rules written on weathered rock
[02:28.64]There were chasers of light
[02:31.03]Find the way or get lost
[02:32.89]We have no way to know
[02:35.28]Where they all headed for
[02:37.66]See a light from afar
[02:39.53]Just blaze through the thorns
[02:42.18]We know it’s right over there
[02:44.04]We have something to declare
[02:46.16]Whatever is arriving, we’ll be prepared
[02:58.39]We’ve made a choice
[02:59.18]Go fight against your fate!
[03:02.90]Pain will come with the blade
[03:06.36]Pain will wake up the despondent crowd in this dormant world somehow
[03:16.46]Unsheathe a sword not to kill
[03:19.64]Unsheathe a sword to rend those clouds above the ground
[03:25.21]Wake up to hear the cheering sound
[03:32.00]✦ 存护与开拓的钢铁意志 ✦`
    },
    {
      id: '3',
      title: '使一颗心免于哀伤',
      artist: '知更鸟 · Chevy · HOYO-MiX',
      cover: '/images/hsr/march7th_selfie.webp',
      url: '/audio/robin_heart.mp3',
      theme: '匹诺康尼 · 知更鸟之歌',
      lyrics: `[00:00.00]使一颗心免于哀伤 (If I Can Stop One Heart From Breaking) - 知更鸟 (Chevy) / HOYO-MiX
[00:13.09]Birds are born with no shackles
[00:18.69]Then what fetters my fate?
[00:24.96]Blown away, the white petals
[00:30.15]Leave me trapped in the cage.
[00:36.34]The endless isolation
[00:39.52]Can't wear down my illusion
[00:42.45]Someday, I’ll make a dream unchained
[00:49.26]Let my heart bravely spread the wings
[00:53.28]Soaring past the night
[00:56.16]To trace the bright moonlight
[01:01.51]Let the clouds heal me of the stings
[01:05.34]Gently wipe the sorrow off my life
[01:09.50]I dream
[01:18.50]What is meant by “miracle”,
[01:24.68]A word outside my days?
[01:30.98]Once again, repeat warbles
[01:36.16]But how could I escape?
[01:42.83]No further hesitation
[01:45.64]On those unanswered questions
[01:48.63]So now, I’ll make a dream unchained
[02:00.19]Let my heart bravely spread the wings
[02:05.39]Soaring past the night
[02:08.02]To trace the bright moonlight
[02:13.32]Let the clouds heal me of the stings
[02:17.20]Gently wipe the sorrow off my life
[02:21.72]I dream
[02:37.37]Let my heart bravely spread the wings
[02:41.11]Soaring past the night
[02:44.04]To trace the bright moonlight
[02:49.28]Let the clouds heal me of the stings
[02:53.31]Gently wipe the sorrow off my life
[03:00.08]I dream
[03:06.13]I dream
[03:12.00]✦ 歌声在梦境与银河间永恒回荡 ✦`
    },
    {
      id: '4',
      title: '不眠之夜 WHITE NIGHT',
      artist: '张杰 · HOYO-MiX',
      cover: '/images/pompom.webp',
      url: '/audio/white_night.mp3',
      theme: '匹诺康尼 · 盛会之星主题歌',
      lyrics: `[00:00.00]不眠之夜 WHITE NIGHT - 张杰 / HOYO-MiX
[00:15.74]车窗外 这夜色 流光溢彩
[00:19.61]别忘了 闭上眼 才算醒来
[00:23.60]你参演 这场戏 变换姿态
[00:27.44]谜底 结局 我该 怎么猜
[00:32.07]记忆是梦的开场白
[00:36.73]（伤疤被掩盖 昨日还在）
[00:40.37]时间在静候你醒来
[00:46.69]（Take me away）
[00:47.92]别再破碎 别再枯萎
[00:51.73]继续沉醉 自我迂回
[00:55.70]最后品味 永恒的滋味
[00:58.90]下一场那夜的梦 再相会
[01:04.01]越是虚伪 越是完美
[01:07.60]美梦入睡 绝望轮回
[01:11.63]一闭一睁 便开始倒退
[01:15.17]下一场那夜的梦 再相会
[01:24.89]（伏笔没解开 悬念还在）
[01:28.51]时间在静候你醒来
[01:34.86]（Sing with me）
[01:35.90]别再破碎 别再枯萎
[01:39.72]继续沉醉 自我迂回
[01:43.53]最后品味 永恒的滋味
[01:46.81]来一场不眠之夜 作结尾
[01:51.99]越要快乐 越要破溃
[01:55.65]是是非非 别再意会
[01:59.62]忘记时间 来梦的派对
[02:02.92]来一场不眠之夜 作结尾
[02:10.00]✦ 欢迎来到盛会之星 匹诺康尼 ✦`
    },
    {
      id: '5',
      title: '太空漫步 Space Walk',
      artist: 'HOYO-MiX',
      cover: '/images/hsr/welt.webp',
      url: '/audio/space_walk.mp3',
      theme: '黑塔空间站 · 纯享氛围原声',
      lyrics: `[00:00.00]太空漫步 Space Walk - HOYO-MiX
[00:05.00]✦ 黑塔空间站 · 奇物主控舱段 ✦
[00:15.00]✦ 纯音乐 · 请享受星际漫步的静谧节奏 ✦
[00:30.00]✦ 虚数引擎平稳巡航中 ✦
[00:50.00]✦ 观测星云流动，记录引力波纹 ✦
[01:10.00]✦ 愿此行，终抵群星 ✦`
    }
  ]);

  const currentIndex = ref(0);
  const currentSong = computed<Song>(() => playlist.value[currentIndex.value] || playlist.value[0]);
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
