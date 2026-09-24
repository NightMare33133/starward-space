import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Song } from '@/types';

export const useMusicStore = defineStore('music', () => {
  const isPlaying = ref(false);
  const isExpanded = ref(false);
  const currentTime = ref(0);
  const duration = ref(0);
  const volume = ref(0.7);

  // 星穹列车留声机精选唱片歌单 (Astral Express Phonograph)
  const playlist = ref<Song[]>([
    {
      id: '1',
      title: 'Take the Journey (踏上旅途)',
      artist: 'HOYO-MiX · Anthony Lynch',
      cover: '/images/hsr/himeko_express.png',
      url: '/audio/take_the_journey.mp3',
    },
    {
      id: '2',
      title: '野火 Wildfire (造物引擎)',
      artist: 'HOYO-MiX · Jonathan Steingard',
      cover: '/images/hsr/danheng.png',
      url: '/audio/wildfire.mp3',
    },
    {
      id: '3',
      title: '太空漫步 Space Walk',
      artist: 'HOYO-MiX',
      cover: '/images/hsr/march7th_selfie.png',
      url: '/audio/space_walk.mp3',
    },
    {
      id: '4',
      title: '猎手的预视 Hunter\'s Intuition',
      artist: 'HOYO-MiX (卡芙卡战斗曲)',
      cover: '/images/hsr/welt.png',
      url: '/audio/hunters_intuition.mp3',
    }
  ]);

  const currentIndex = ref(0);
  const currentSong = ref<Song>(playlist.value[0]);

  const audioElement = ref<HTMLAudioElement | null>(null);

  const initAudio = () => {
    if (!audioElement.value) {
      audioElement.value = new Audio(currentSong.value.url);
      audioElement.value.volume = volume.value;
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
        next();
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
    const nextIdx = (currentIndex.value + 1) % playlist.value.length;
    switchSong(nextIdx);
  };

  const prev = () => {
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

  return {
    isPlaying,
    isExpanded,
    currentTime,
    duration,
    volume,
    playlist,
    currentSong,
    currentIndex,
    play,
    pause,
    togglePlay,
    next,
    prev,
    seek,
    switchSong,
  };
});
