import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(true);

  // 初始化深浅色主题
  const initTheme = () => {
    const saved = localStorage.getItem('starward_theme');
    if (saved) {
      isDark.value = saved === 'dark';
    } else {
      isDark.value = true; // 默认深空暗黑主题
    }
    applyTheme();
  };

  const toggleTheme = () => {
    isDark.value = !isDark.value;
    localStorage.setItem('starward_theme', isDark.value ? 'dark' : 'light');
    applyTheme();
  };

  const applyTheme = () => {
    if (isDark.value) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  };

  return {
    isDark,
    initTheme,
    toggleTheme,
  };
});
