<template>
  <div class="min-h-[80vh] flex items-center justify-center px-4 py-12">
    <div class="w-full max-w-md p-8 rounded-3xl glass-island border border-amber-400/30 shadow-2xl shadow-amber-500/10 relative overflow-hidden text-slate-800 dark:text-slate-100">
      <!-- 顶部星际流光装饰线 -->
      <div class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan"></div>

      <!-- 帕姆全息头像与标题 -->
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
        <h2 class="text-2xl font-bold tracking-wide text-slate-800 dark:text-white">列车长身份认证</h2>
        <p class="text-xs text-slate-500 dark:text-slate-400 mt-1.5">
          请验证管理员凭证以解锁星向空间全量管理权限
        </p>
      </div>

      <!-- 若已存在有效会话：快捷直接跃迁卡片 -->
      <div v-if="currentUser" class="mb-6 p-4 rounded-2xl bg-emerald-500/10 border border-emerald-500/20 text-slate-200">
        <div class="flex items-center space-x-3 mb-3">
          <div class="w-10 h-10 rounded-xl overflow-hidden bg-space-900 border border-emerald-400/30 flex items-center justify-center shrink-0">
            <img :src="currentUser.avatar || '/images/pompom.webp'" alt="avatar" class="w-full h-full object-cover" />
          </div>
          <div class="min-w-0 flex-1">
            <div class="text-sm font-bold text-emerald-400 flex items-center space-x-1">
              <span>{{ currentUser.nickname || currentUser.username }}</span>
              <span class="text-[10px] px-1.5 py-0.2 rounded bg-emerald-500/20 border border-emerald-500/30 text-emerald-300">
                {{ currentUser.role }}
              </span>
            </div>
            <div class="text-xs text-slate-400 truncate">@{{ currentUser.username }} · 已保存登录令牌</div>
          </div>
        </div>
        <div class="flex items-center space-x-2">
          <button
            @click="enterDirectly"
            class="flex-1 py-2 px-3 rounded-xl bg-emerald-500 hover:bg-emerald-600 text-white text-xs font-semibold shadow-md shadow-emerald-500/20 transition-all flex items-center justify-center space-x-1"
          >
            <ShieldCheck class="w-3.5 h-3.5" />
            <span>直接跃迁进入中枢</span>
          </button>
          <button
            @click="logoutAndReset"
            class="py-2 px-3 rounded-xl bg-white/5 hover:bg-white/10 text-slate-400 hover:text-white text-xs transition-all"
          >
            切换账号
          </button>
        </div>
      </div>

      <!-- 登录表单 -->
      <form v-else @submit.prevent="handleLogin" class="space-y-4">
        <!-- 账号输入 -->
        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1.5 flex items-center space-x-1.5">
            <UserIcon class="w-3.5 h-3.5 text-amber-500 dark:text-amber-400" />
            <span>登录账号</span>
          </label>
          <input
            v-model="username"
            type="text"
            placeholder="请输入管理员账号..."
            autocomplete="username"
            class="w-full px-4 py-2.5 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-slate-800 dark:text-slate-100 text-sm placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 focus:ring-1 focus:ring-amber-400/50 transition-all font-mono"
            autofocus
          />
        </div>

        <!-- 密码输入 -->
        <div>
          <label class="block text-xs font-medium text-slate-700 dark:text-slate-300 mb-1.5 flex items-center space-x-1.5">
            <Lock class="w-3.5 h-3.5 text-amber-500 dark:text-amber-400" />
            <span>登录密码</span>
          </label>
          <div class="relative">
            <input
              v-model="password"
              :type="showPassword ? 'text' : 'password'"
              placeholder="请输入星轨认证密码..."
              autocomplete="current-password"
              class="w-full px-4 py-2.5 rounded-xl bg-white/80 dark:bg-space-950/80 border border-pink-200/70 dark:border-white/10 text-slate-800 dark:text-slate-100 text-sm placeholder-slate-400 dark:placeholder-slate-500 focus:outline-none focus:border-amber-500 dark:focus:border-amber-400/80 focus:ring-1 focus:ring-amber-400/50 transition-all font-mono"
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
          :disabled="loading || !username.trim() || !password"
          class="w-full mt-2 py-3 rounded-xl bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan text-white text-sm font-bold shadow-lg shadow-amber-500/25 hover:opacity-95 active:scale-[0.98] disabled:opacity-50 disabled:pointer-events-none transition-all flex items-center justify-center space-x-2"
        >
          <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
          <ShieldCheck v-else class="w-4 h-4" />
          <span>{{ loading ? '正在验证跃迁许可...' : '跃迁登录中枢' }}</span>
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
import { User as UserIcon, Lock, Eye, EyeOff, Sparkles, Loader2, AlertCircle, ShieldCheck, ArrowLeft } from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { login, getCurrentUser } from '@/api/admin';
import type { User } from '@/types';

const router = useRouter();
const route = useRoute();
const username = ref('');
const password = ref('');
const showPassword = ref(false);
const loading = ref(false);
const errorMessage = ref('');
const currentUser = ref<User | null>(null);

onMounted(async () => {
  const storedToken = localStorage.getItem('starward_admin_token');
  if (storedToken) {
    try {
      const user = await getCurrentUser(storedToken);
      if (user && user.role === 'ADMIN') {
        currentUser.value = user;
      }
    } catch {
      localStorage.removeItem('starward_admin_token');
    }
  }
});

const enterDirectly = () => {
  const redirectPath = (route.query.redirect as string) || '/admin/posts';
  router.push(redirectPath);
};

const logoutAndReset = () => {
  localStorage.removeItem('starward_admin_token');
  localStorage.removeItem('starward_user');
  currentUser.value = null;
  username.value = '';
  password.value = '';
};

const handleLogin = async () => {
  const u = username.value.trim();
  const p = password.value;
  if (!u || !p) return;

  loading.value = true;
  errorMessage.value = '';

  try {
    const res = await login(u, p);
    loading.value = false;

    if (res && res.token) {
      if (res.user.role !== 'ADMIN') {
        errorMessage.value = '权限受限：该账号不具备管理员控制权限';
        return;
      }

      localStorage.setItem('starward_admin_token', res.token);
      localStorage.setItem('starward_user', JSON.stringify(res.user));

      confetti({
        particleCount: 70,
        spread: 60,
        origin: { y: 0.6 },
        colors: ['#38bdf8', '#f59e0b', '#ec4899', '#10b981'],
      });

      const redirectPath = (route.query.redirect as string) || '/admin/posts';
      router.push(redirectPath);
    }
  } catch (err: any) {
    loading.value = false;
    errorMessage.value = err.message || '跃迁密令校验失败：账号或密码错误！';
  }
};
</script>
