<template>
  <div class="fixed inset-0 pointer-events-none z-0 overflow-hidden bg-gradient-to-br from-[#fff7f9] via-[#fdf4f7] to-[#f4f2ff] transition-colors duration-1000">
    <!-- 晨曦温润光晕层（落樱粉、晨曦金与柔紫晨雾） -->
    <div class="absolute -top-[15%] -left-[10%] w-[55vw] h-[55vw] rounded-full bg-pink-300/25 blur-[140px] pointer-events-none"></div>
    <div class="absolute top-[20%] -right-[15%] w-[60vw] h-[60vw] rounded-full bg-rose-200/30 blur-[160px] pointer-events-none"></div>
    <div class="absolute top-[60%] left-[10%] w-[45vw] h-[45vw] rounded-full bg-amber-200/20 blur-[150px] pointer-events-none"></div>
    <div class="absolute -bottom-[20%] right-[20%] w-[55vw] h-[55vw] rounded-full bg-purple-200/20 blur-[160px] pointer-events-none"></div>

    <!-- 落樱飘舞与晨光微粒 Canvas -->
    <canvas ref="canvasRef" class="w-full h-full block relative z-10"></canvas>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const canvasRef = ref<HTMLCanvasElement | null>(null);
let animationFrameId: number;

interface SakuraPetal {
  x: number;
  y: number;
  size: number;
  vx: number;
  vy: number;
  rotation: number;
  rotationSpeed: number;
  flip: number;
  flipSpeed: number;
  tilt: number;
  swayPhase: number;
  swaySpeed: number;
  swayWidth: number;
  alpha: number;
  color: {
    r: number;
    g: number;
    b: number;
  };
}

interface SunGlimmer {
  x: number;
  y: number;
  radius: number;
  alpha: number;
  alphaSpeed: number;
  vx: number;
  vy: number;
}

const petals: SakuraPetal[] = [];
const glimmers: SunGlimmer[] = [];
const petalCount = 42; // 落樱花瓣密度
const glimmerCount = 28; // 晨光微光粒子

let mouseX = 0;
let mouseY = 0;

// 樱花瓣自然调色盘
const sakuraPalettes = [
  { r: 244, g: 114, b: 182 }, // 经典暖粉 (#f472b6)
  { r: 251, g: 146, b: 160 }, // 柔桃初樱 (#fb92a0)
  { r: 249, g: 168, b: 212 }, // 淡粉花蕊 (#f9a8d4)
  { r: 253, g: 164, b: 175 }, // 晨曦玫瑰粉 (#fda4af)
  { r: 255, g: 214, b: 228 }, // 雪粉初绽 (#ffd6e4)
];

const initParticles = (width: number, height: number) => {
  petals.length = 0;
  for (let i = 0; i < petalCount; i++) {
    const palette = sakuraPalettes[Math.floor(Math.random() * sakuraPalettes.length)];
    petals.push({
      x: Math.random() * (width + 200) - 100,
      y: Math.random() * height,
      size: Math.random() * 8 + 10, // 10px ~ 18px
      vx: Math.random() * 0.8 + 0.6, // 微风向右吹拂
      vy: Math.random() * 1.2 + 0.9, // 缓降
      rotation: Math.random() * Math.PI * 2,
      rotationSpeed: (Math.random() - 0.5) * 0.02,
      flip: Math.random() * Math.PI * 2,
      flipSpeed: Math.random() * 0.03 + 0.015,
      tilt: Math.random() * Math.PI,
      swayPhase: Math.random() * Math.PI * 2,
      swaySpeed: Math.random() * 0.025 + 0.01,
      swayWidth: Math.random() * 2.2 + 1.0,
      alpha: Math.random() * 0.4 + 0.55, // 0.55 ~ 0.95
      color: palette,
    });
  }

  glimmers.length = 0;
  for (let i = 0; i < glimmerCount; i++) {
    glimmers.push({
      x: Math.random() * width,
      y: Math.random() * height,
      radius: Math.random() * 1.8 + 0.8,
      alpha: Math.random() * 0.6 + 0.2,
      alphaSpeed: (Math.random() * 0.015 + 0.006) * (Math.random() > 0.5 ? 1 : -1),
      vx: Math.random() * 0.4 + 0.1,
      vy: -(Math.random() * 0.3 + 0.05), // 晨光粒子如微尘缓缓浮升
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
    initParticles(canvas.width, canvas.height);
  };

  resize();
  window.addEventListener('resize', resize);
  window.addEventListener('mousemove', handleMouseMove);

  const render = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // 1. 绘制晨光金粉微粒
    for (const g of glimmers) {
      g.alpha += g.alphaSpeed;
      if (g.alpha > 0.85 || g.alpha < 0.15) {
        g.alphaSpeed = -g.alphaSpeed;
      }

      g.x += g.vx;
      g.y += g.vy;

      if (g.y < -10) {
        g.y = canvas.height + 10;
        g.x = Math.random() * canvas.width;
      }
      if (g.x > canvas.width + 10) g.x = -10;

      ctx.save();
      ctx.beginPath();
      ctx.arc(g.x + mouseX * 0.4, g.y + mouseY * 0.4, g.radius, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(251, 191, 36, ${g.alpha * 0.7})`;
      ctx.shadowBlur = 8;
      ctx.shadowColor = 'rgba(251, 191, 36, 0.6)';
      ctx.fill();
      ctx.restore();
    }

    // 2. 绘制 3D 拟真落樱花瓣
    for (const p of petals) {
      // 物理运动与风力摆动
      p.swayPhase += p.swaySpeed;
      const swayOffset = Math.sin(p.swayPhase) * p.swayWidth;
      p.x += p.vx + swayOffset + mouseX * 0.3;
      p.y += p.vy + mouseY * 0.2;

      p.rotation += p.rotationSpeed;
      p.flip += p.flipSpeed;

      // 越出屏幕时重置至顶端或左侧
      if (p.y > canvas.height + 30) {
        p.y = -25;
        p.x = Math.random() * (canvas.width + 150) - 100;
      }
      if (p.x > canvas.width + 50) {
        p.x = -30;
      }

      // 渲染单朵花瓣
      ctx.save();
      ctx.translate(p.x, p.y);
      ctx.rotate(p.rotation);

      // 3D 空间翻转投影：Math.cos(p.flip) 模拟花瓣在空中翻转
      const flipScale = Math.cos(p.flip);
      const tiltScale = 0.7 + 0.3 * Math.sin(p.tilt);
      ctx.scale(flipScale, tiltScale);

      ctx.beginPath();
      const s = p.size;
      // 真实樱花花瓣贝塞尔曲线建模 (带微凹花尖)
      ctx.moveTo(0, -s);
      ctx.bezierCurveTo(-s * 0.85, -s * 0.4, -s * 0.75, s * 0.65, 0, s);
      ctx.bezierCurveTo(s * 0.75, s * 0.65, s * 0.85, -s * 0.4, 0, -s);
      ctx.closePath();

      // 渐变填充：由花瓣根部白粉向瓣尖娇艳粉晕染
      const grad = ctx.createLinearGradient(0, -s, 0, s);
      grad.addColorStop(0, `rgba(${p.color.r}, ${p.color.g}, ${p.color.b}, ${p.alpha})`);
      grad.addColorStop(0.7, `rgba(${p.color.r + 8}, ${p.color.g + 18}, ${p.color.b + 18}, ${p.alpha * 0.85})`);
      grad.addColorStop(1, `rgba(255, 250, 252, ${p.alpha * 0.6})`);

      ctx.fillStyle = grad;
      ctx.shadowColor = `rgba(${p.color.r}, ${p.color.g}, ${p.color.b}, 0.25)`;
      ctx.shadowBlur = 4;
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
