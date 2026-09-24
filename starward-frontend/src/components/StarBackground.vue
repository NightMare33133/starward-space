<template>
  <div class="fixed inset-0 pointer-events-none z-0 overflow-hidden">
    <!-- 底层高清星穹壁纸（带柔和暗色蒙版与深度模糊） -->
    <div
      class="absolute inset-0 bg-cover bg-center bg-no-repeat transition-all duration-1000 transform scale-105"
      :style="{
        backgroundImage: `url('/images/hsr/himeko_express.png')`,
        filter: 'brightness(0.35) saturate(1.2)'
      }"
    ></div>

    <!-- 弥散色彩光晕层（星轨紫与深空蓝） -->
    <div class="absolute -top-[15%] -left-[10%] w-[55vw] h-[55vw] rounded-full bg-nebula-cyan/10 blur-[140px]"></div>
    <div class="absolute top-[35%] -right-[15%] w-[60vw] h-[60vw] rounded-full bg-nebula-purple/15 blur-[160px]"></div>
    <div class="absolute -bottom-[20%] left-[25%] w-[50vw] h-[50vw] rounded-full bg-amber-500/10 blur-[150px]"></div>

    <!-- 暗夜微粒画布：星光 + 缓缓升腾的金色流萤粒子 -->
    <canvas ref="canvasRef" class="w-full h-full block relative z-10"></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const canvasRef = ref<HTMLCanvasElement | null>(null);
let animationFrameId: number;

interface Firefly {
  x: number;
  y: number;
  radius: number;
  alpha: number;
  alphaSpeed: number;
  vx: number;
  vy: number;
  hue: number;
}

const fireflies: Firefly[] = [];
const count = 65; // 流萤与星芒总数
let mouseX = 0;
let mouseY = 0;

const initFireflies = (width: number, height: number) => {
  fireflies.length = 0;
  for (let i = 0; i < count; i++) {
    fireflies.push({
      x: Math.random() * width,
      y: Math.random() * height,
      radius: Math.random() * 2.2 + 0.8,
      alpha: Math.random() * 0.7 + 0.2,
      alphaSpeed: (Math.random() * 0.015 + 0.005) * (Math.random() > 0.5 ? 1 : -1),
      vx: (Math.random() - 0.5) * 0.3,
      vy: -(Math.random() * 0.4 + 0.15), // 缓缓向上升腾，如流萤
      hue: Math.random() > 0.4 ? 170 : 45, // 青蓝星芒 vs 暖金流萤
    });
  }
};

const handleMouseMove = (e: MouseEvent) => {
  mouseX = (e.clientX - window.innerWidth / 2) * 0.02;
  mouseY = (e.clientY - window.innerHeight / 2) * 0.02;
};

onMounted(() => {
  const canvas = canvasRef.value;
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  if (!ctx) return;

  const resize = () => {
    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;
    initFireflies(canvas.width, canvas.height);
  };

  resize();
  window.addEventListener('resize', resize);
  window.addEventListener('mousemove', handleMouseMove);

  const render = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for (let f of fireflies) {
      // 呼吸发光
      f.alpha += f.alphaSpeed;
      if (f.alpha > 0.95 || f.alpha < 0.15) {
        f.alphaSpeed = -f.alphaSpeed;
      }

      // 运动位移
      f.x += f.vx;
      f.y += f.vy;

      // 循环回到底部
      if (f.y < -10) {
        f.y = canvas.height + 10;
        f.x = Math.random() * canvas.width;
      }
      if (f.x < -10) f.x = canvas.width + 10;
      if (f.x > canvas.width + 10) f.x = -10;

      // 鼠标微视差
      const renderX = f.x + mouseX * f.radius;
      const renderY = f.y + mouseY * f.radius;

      ctx.save();
      ctx.beginPath();
      ctx.arc(renderX, renderY, f.radius, 0, Math.PI * 2);

      // 发光粒子渐变
      const gradient = ctx.createRadialGradient(
        renderX, renderY, 0,
        renderX, renderY, f.radius * 3.5
      );
      if (f.hue === 45) {
        // 暖金流萤（萤火虫）
        gradient.addColorStop(0, `rgba(253, 224, 71, ${f.alpha})`);
        gradient.addColorStop(0.5, `rgba(234, 179, 8, ${f.alpha * 0.6})`);
        gradient.addColorStop(1, 'rgba(234, 179, 8, 0)');
      } else {
        // 冰蓝星芒
        gradient.addColorStop(0, `rgba(186, 230, 253, ${f.alpha})`);
        gradient.addColorStop(0.5, `rgba(56, 189, 248, ${f.alpha * 0.5})`);
        gradient.addColorStop(1, 'rgba(56, 189, 248, 0)');
      }

      ctx.fillStyle = gradient;
      ctx.shadowBlur = 10;
      ctx.shadowColor = f.hue === 45 ? 'rgba(250, 204, 21, 0.8)' : 'rgba(56, 189, 248, 0.8)';
      ctx.fill();
      ctx.restore();
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
