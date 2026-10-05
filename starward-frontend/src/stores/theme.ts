import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(true);
  const burstCount = ref(0);
  const showAmbientFlash = ref(false);

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

    // 触发局部粒子光环爆裂与瞬态环境微光
    burstCount.value++;
    showAmbientFlash.value = true;
    setTimeout(() => {
      showAmbientFlash.value = false;
    }, 450);
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
    burstCount,
    showAmbientFlash,
    initTheme,
    toggleTheme,
  };
});
