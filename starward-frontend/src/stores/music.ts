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
      title: 'Take the Journey (星穹列车启航)',
      artist: 'HOYO-MiX',
      cover: '/images/hsr/himeko_express.png',
      url: 'https://actions.google.com/sounds/v1/ambiences/rain_heavy.ogg',
    },
    {
      id: '2',
      title: '野火 Wildfire (雅利洛之燃)',
      artist: 'HOYO-MiX',
      cover: '/images/hsr/danheng.png',
      url: 'https://actions.google.com/sounds/v1/weather/light_rain_on_car.ogg',
    },
    {
      id: '3',
      title: '这就是我啦! (三月七的自拍手记)',
      artist: 'HOYO-MiX',
      cover: '/images/hsr/march7th_selfie.png',
      url: 'https://actions.google.com/sounds/v1/water/waves_crashing.ogg',
    },
    {
      id: '4',
      title: '以世界之名 (瓦尔特的重力波)',
      artist: 'HOYO-MiX',
      cover: '/images/hsr/welt.png',
      url: 'https://actions.google.com/sounds/v1/ambiences/rain_heavy.ogg',
    }
  ]);

  const currentIndex = ref(0);
  const currentSong = ref<Song>(playlist.value[0]);

  const audioElement = ref<HTMLAudioElement | null>(null);

  const initAudio = () => {
    if (!audioElement.value) {
      audioElement.value = new Audio(currentSong.value.url);
      audioElement.value.volume = volume.value;

      audioElement.value.addEventListener('timeupdate', () => {
        if (audioElement.value) {
          currentTime.value = audioElement.value.currentTime;
          duration.value = audioElement.value.duration || 0;
        }
      });

      audioElement.value.addEventListener('ended', () => {
        next();
      });
    }
  };

  const play = () => {
    initAudio();
    if (audioElement.value) {
      audioElement.value.play().then(() => {
        isPlaying.value = true;
      }).catch(err => {
        console.warn('Audio auto-play blocked by browser policy:', err);
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
    if (audioElement.value) {
      audioElement.value.src = currentSong.value.url;
      if (isPlaying.value) {
        audioElement.value.play();
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
