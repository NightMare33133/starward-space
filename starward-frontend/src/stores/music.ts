import { defineStore } from 'pinia';
import { ref } from 'vue';
import type { Song } from '@/types';

export const useMusicStore = defineStore('music', () => {
  const isPlaying = ref(false);
  const isExpanded = ref(false);
  const currentTime = ref(0);
  const duration = ref(0);
  const volume = ref(0.7);

  // 精选星际氛围与深夜写码歌单
  const playlist = ref<Song[]>([
    {
      id: '1',
      title: 'Voyage to the Stars',
      artist: 'Starlight Ambient',
      cover: 'https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86?w=300&auto=format&fit=crop&q=80',
      url: 'https://actions.google.com/sounds/v1/ambiences/rain_heavy.ogg', // 真实免版权高音质流
    },
    {
      id: '2',
      title: 'Midnight Code & Tea',
      artist: 'Lo-Fi Chill Beat',
      cover: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=300&auto=format&fit=crop&q=80',
      url: 'https://actions.google.com/sounds/v1/weather/light_rain_on_car.ogg',
    },
    {
      id: '3',
      title: 'Interstellar Drift',
      artist: 'Synthwave Odyssey',
      cover: 'https://images.unsplash.com/photo-1451187580459-43490279c0fa?w=300&auto=format&fit=crop&q=80',
      url: 'https://actions.google.com/sounds/v1/water/waves_crashing.ogg',
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
