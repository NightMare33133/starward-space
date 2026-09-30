<template>
  <div class="min-h-[80vh] flex items-center justify-center px-4 py-12">
    <div class="w-full max-w-md p-8 rounded-3xl glass-island border border-amber-400/30 shadow-2xl shadow-amber-500/10 relative overflow-hidden text-slate-800 dark:text-slate-100">
      <!-- 顶部星际流光装饰线 -->
      <div class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan"></div>

      <!-- 帕姆全息头像 -->
      <div class="flex flex-col items-center text-center mb-8">
        <div class="w-24 h-24 mb-4 rounded-3xl bg-gradient-to-tr from-amber-400 via-nebula-pink to-nebula-cyan p-[2px] shadow-xl shadow-amber-500/25">
          <div class="w-full h-full rounded-[22px] overflow-hidden bg-pink-100 dark:bg-space-950 flex items-center justify-center">
            <img
              src="/images/pompom.webp"
              alt="Pom-Pom Conductor"
              class="w-full h-full object-cover hover:scale-105 transition-transform"
            />
          </div>
        </div>
        <div class="inline-flex items-center space-x-1.5 px-3 py-1 rounded-full bg-amber-400/15 border border-amber-400/30 text-amber-700 dark:text-amber-300 text-xs font-semibold mb-2">
          <Sparkles class="w-3.5 h-3.5" />
          <span>星穹列车 · 控制中枢</span>
        </div>
        <h2 class="text-2xl font-bold tracking-wide text-slate-800 dark:text-white">列车长密令认证</h2>
        <p class="text-xs text-slate-500 dark:text-slate-400 mt-1.5">
          请输入跃迁密钥以解锁博客全量管理与内容编辑权限
        </p>
      </div>

      <!-- 表单输入 -->
      <form @submit.prevent="handleLogin" class="space-y-5">
        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-2 flex items-center justify-between">
            <span class="flex items-center space-x-1.5">
              <Key class="w-4 h-4 text-amber-500 dark:text-amber-400" />
              <span>管理员密钥 (X-Admin-Token)</span>
            </span>
            <span class="text-[10px] text-slate-500">默认: starward-secret-token-2026</span>
          </label>
          <div class="relative">
            <input
              v-model="tokenInput"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入星轨管理密钥..."
              class="w-full px-4 py-3 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-slate-800 dark:text-slate-100 text-sm placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 focus:ring-1 focus:ring-amber-400/50 transition-all font-mono"
              autofocus
            />
            <button
              type="button"
              @click="showPassword = !showPassword"
              class="absolute right-3.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 transition-colors"
            >
              <Eye v-if="!showPassword" class="w-4 h-4" />
              <EyeOff v-else class="w-4 h-4" />
            </button>
          </div>
        </div>

        <!-- 错误提示 -->
        <div v-if="errorMessage" class="p-3 rounded-xl bg-rose-500/10 border border-rose-500/20 text-xs text-rose-300 flex items-center space-x-2">
          <AlertCircle class="w-4 h-4 shrink-0" />
          <span>{{ errorMessage }}</span>
        </div>

        <!-- 跃迁按钮 -->
        <button
          type="submit"
          :disabled="loading || !tokenInput.trim()"
          class="w-full py-3 rounded-xl bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan text-white text-sm font-bold shadow-lg shadow-amber-500/25 hover:opacity-95 active:scale-[0.98] disabled:opacity-50 disabled:pointer-events-none transition-all flex items-center justify-center space-x-2"
        >
          <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
          <ShieldCheck v-else class="w-4 h-4" />
          <span>{{ loading ? '正在验证跃迁许可...' : '跃迁进入中枢' }}</span>
        </button>

        <!-- 返回主页 -->
        <div class="text-center pt-2">
          <router-link
            to="/"
            class="text-xs text-slate-400 hover:text-nebula-cyan transition-colors inline-flex items-center space-x-1"
          >
            <ArrowLeft class="w-3.5 h-3.5" />
            <span>返回星向空间前台</span>
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { Key, Eye, EyeOff, Sparkles, Loader2, AlertCircle, ShieldCheck, ArrowLeft } from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { verifyAdminToken } from '@/api/admin';

const router = useRouter();
const route = useRoute();
const tokenInput = ref('');
const showPassword = ref(false);
const loading = ref(false);
const errorMessage = ref('');

onMounted(() => {
  const stored = localStorage.getItem('starward_admin_token');
  if (stored) {
    tokenInput.value = stored;
  }
});

const handleLogin = async () => {
  const token = tokenInput.value.trim();
  if (!token) return;

  loading.value = true;
  errorMessage.value = '';

  const isValid = await verifyAdminToken(token);
  loading.value = false;

  if (isValid) {
    localStorage.setItem('starward_admin_token', token);
    confetti({
      particleCount: 70,
      spread: 60,
      origin: { y: 0.6 },
      colors: ['#38bdf8', '#f59e0b', '#ec4899', '#10b981'],
    });

    const redirectPath = (route.query.redirect as string) || '/admin/posts';
    router.push(redirectPath);
  } else {
    errorMessage.value = '跃迁密令校验失败：未授权访问或密钥错误！';
  }
};
</script>
