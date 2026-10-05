import { defineStore } from 'pinia';
import { ref, nextTick } from 'vue';

export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(true);
  const isTransitioning = ref(false);

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

  const applyTheme = () => {
    if (isDark.value) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  };

  const toggleTheme = (event?: MouseEvent) => {
    // 动画运行中互斥防重
    if (isTransitioning.value) return;

    // 检查浏览器是否支持 View Transition API 以及用户是否偏好减少动效
    const isAppearanceTransition =
      typeof document !== 'undefined' &&
      'startViewTransition' in document &&
      !window.matchMedia('(prefers-reduced-motion: reduce)').matches;

    if (!isAppearanceTransition) {
      isDark.value = !isDark.value;
      localStorage.setItem('starward_theme', isDark.value ? 'dark' : 'light');
      applyTheme();
      return;
    }

    // 精确提取点击波纹发起的锚点坐标 (支持鼠标点击、触摸与键盘聚焦)
    let x = window.innerWidth / 2;
    let y = window.innerHeight / 2;

    if (event && (event.clientX !== 0 || event.clientY !== 0)) {
      x = event.clientX;
      y = event.clientY;
    } else if (event?.target instanceof HTMLElement) {
      const rect = event.target.getBoundingClientRect();
      x = rect.left + rect.width / 2;
      y = rect.top + rect.height / 2;
    }

    // 计算到屏幕四个角的最远距离（确保圆形扩散能完整覆盖整个屏幕视口）
    const endRadius = Math.hypot(
      Math.max(x, window.innerWidth - x),
      Math.max(y, window.innerHeight - y)
    );

    isTransitioning.value = true;

    const transition = (document as any).startViewTransition(async () => {
      isDark.value = !isDark.value;
      localStorage.setItem('starward_theme', isDark.value ? 'dark' : 'light');
      applyTheme();
      await nextTick();
      // 等待一帧动画帧，确保 DOM 与 Canvas 粒子初始状态绘制完毕再生成快照
      await new Promise((resolve) => requestAnimationFrame(resolve));
    });

    transition.ready
      .then(() => {
        const clipPath = [
          `circle(0px at ${x}px ${y}px)`,
          `circle(${endRadius}px at ${x}px ${y}px)`,
        ];

        document.documentElement.animate(
          {
            clipPath: clipPath,
          },
          {
            duration: 650,
            easing: 'cubic-bezier(0.22, 1, 0.36, 1)',
            pseudoElement: '::view-transition-new(root)',
          }
        );
      })
      .finally(() => {
        transition.finished.finally(() => {
          isTransitioning.value = false;
        });
      });
  };

  return {
    isDark,
    isTransitioning,
    initTheme,
    toggleTheme,
  };
});
