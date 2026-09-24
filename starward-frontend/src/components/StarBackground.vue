<template>
  <div class="fixed inset-0 pointer-events-none z-0 overflow-hidden">
    <!-- 背景流光星云渐变 -->
    <div class="absolute -top-[20%] -left-[10%] w-[60vw] h-[60vw] rounded-full bg-nebula-cyan/5 blur-[120px] pointer-events-none"></div>
    <div class="absolute top-[40%] -right-[15%] w-[55vw] h-[55vw] rounded-full bg-nebula-purple/5 blur-[140px] pointer-events-none"></div>
    <div class="absolute -bottom-[20%] left-[20%] w-[50vw] h-[50vw] rounded-full bg-nebula-blue/5 blur-[130px] pointer-events-none"></div>

    <!-- Canvas 星空粒子画布 -->
    <canvas ref="canvasRef" class="w-full h-full block opacity-70"></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const canvasRef = ref<HTMLCanvasElement | null>(null);
let animationFrameId: number;

interface Star {
  x: number;
  y: number;
  radius: number;
  alpha: number;
  alphaSpeed: number;
  vx: number;
  vy: number;
}

const stars: Star[] = [];
const starCount = 120;
let mouseX = 0;
let mouseY = 0;

const initStars = (width: number, height: number) => {
  stars.length = 0;
  for (let i = 0; i < starCount; i++) {
    stars.push({
      x: Math.random() * width,
      y: Math.random() * height,
      radius: Math.random() * 1.5 + 0.3,
      alpha: Math.random() * 0.8 + 0.2,
      alphaSpeed: (Math.random() * 0.01 + 0.003) * (Math.random() > 0.5 ? 1 : -1),
      vx: (Math.random() - 0.5) * 0.15,
      vy: (Math.random() - 0.5) * 0.15,
    });
  }
};

const handleMouseMove = (e: MouseEvent) => {
  mouseX = (e.clientX - window.innerWidth / 2) * 0.03;
  mouseY = (e.clientY - window.innerHeight / 2) * 0.03;
};

onMounted(() => {
  const canvas = canvasRef.value;
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  if (!ctx) return;

  const resize = () => {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    initStars(canvas.width, canvas.height);
  };

  resize();
  window.addEventListener('resize', resize);
  window.addEventListener('mousemove', handleMouseMove);

  const render = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for (let star of stars) {
      // 粒子微闪烁
      star.alpha += star.alphaSpeed;
      if (star.alpha > 0.95 || star.alpha < 0.15) {
        star.alphaSpeed = -star.alphaSpeed;
      }

      // 粒子极微弱的宇宙漂移
      star.x += star.vx;
      star.y += star.vy;

      if (star.x < 0) star.x = canvas.width;
      if (star.x > canvas.width) star.x = 0;
      if (star.y < 0) star.y = canvas.height;
      if (star.y > canvas.height) star.y = 0;

      // 随鼠标发生微妙的视差位移
      const renderX = star.x + mouseX * star.radius;
      const renderY = star.y + mouseY * star.radius;

      ctx.beginPath();
      ctx.arc(renderX, renderY, star.radius, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(224, 242, 254, ${star.alpha})`;
      ctx.shadowBlur = star.radius > 1 ? 6 : 0;
      ctx.shadowColor = 'rgba(56, 189, 248, 0.8)';
      ctx.fill();
    }

    animationFrameId = requestAnimationFrame(render);
  };

  render();

  onUnmounted(() => {
    cancelAnimationFrame(animationFrameId);
    window.removeEventListener('resize', resize);
    window.removeEventListener('mousemove', handleMouseMove);
  });
});
</script>
