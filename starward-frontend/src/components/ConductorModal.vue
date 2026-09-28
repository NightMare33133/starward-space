<template>
  <teleport to="body">
    <transition
      enter-active-class="transition duration-300 ease-out"
      enter-from-class="opacity-0 scale-95"
      enter-to-class="opacity-100 scale-100"
      leave-active-class="transition duration-200 ease-in"
      leave-from-class="opacity-100 scale-100"
      leave-to-class="opacity-0 scale-95"
    >
      <div
        v-if="modelValue"
        class="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/75 backdrop-blur-md"
        @click.self="closeModal"
      >
        <!-- 全息密令弹窗主体 -->
        <div
          class="relative w-full max-w-md p-6 sm:p-8 rounded-3xl bg-space-900/90 border border-amber-400/40 shadow-2xl shadow-amber-500/20 text-slate-100 overflow-hidden"
          :class="{ 'animate-shake': shake }"
        >
          <!-- 顶部星际流光装饰线 -->
          <div class="absolute top-0 left-0 right-0 h-1 bg-gradient-to-r from-amber-400 via-nebula-pink to-nebula-cyan"></div>

          <!-- 关闭按钮 -->
          <button
            @click="closeModal"
            class="absolute top-4 right-4 p-2 rounded-xl text-slate-400 hover:text-white hover:bg-white/5 transition-colors"
          >
            <X class="w-5 h-5" />
          </button>

          <!-- 帕姆列车长全息头像与标题 -->
          <div class="flex flex-col items-center text-center mb-6">
            <div class="relative w-20 h-20 mb-3 rounded-2xl bg-gradient-to-tr from-amber-400 via-nebula-pink to-nebula-cyan p-[2px] shadow-xl shadow-amber-500/30">
              <div class="w-full h-full rounded-[14px] overflow-hidden bg-space-950 flex items-center justify-center">
                <img
                  src="/images/pompom.png"
                  alt="Pom-Pom Conductor"
                  class="w-full h-full object-cover hover:scale-110 transition-transform duration-300"
                />
              </div>
              <span class="absolute -bottom-1 -right-1 px-1.5 py-0.5 rounded-full text-[10px] font-bold bg-amber-400 text-space-950 shadow">
                列车长
              </span>
            </div>

            <h3 class="text-xl font-bold tracking-wide text-slate-100 flex items-center space-x-1.5">
              <span>✦ 星轨列车长密令 ✦</span>
            </h3>
            <p class="text-xs text-slate-400 mt-1">
              星穹列车极客中枢 · 请出示跃迁密令以获取控制权限
            </p>
          </div>

          <!-- 提示：如果已登录 -->
          <div v-if="hasExistingToken" class="mb-4 p-3 rounded-xl bg-emerald-500/10 border border-emerald-500/20 text-xs text-emerald-300 flex items-center justify-between">
            <div class="flex items-center space-x-2">
              <ShieldCheck class="w-4 h-4 text-emerald-400 shrink-0" />
              <span>检测到已保存的列车长凭据</span>
            </div>
            <button
              @click="enterDirectly"
              class="px-2.5 py-1 rounded-lg bg-emerald-500/20 hover:bg-emerald-500/30 text-emerald-200 font-medium transition-colors"
            >
              直接跃迁 →
            </button>
          </div>

          <!-- 密令输入框 -->
          <form @submit.prevent="handleVerify" class="space-y-4">
            <div>
              <label class="block text-xs font-medium text-slate-300 mb-1.5 flex items-center justify-between">
                <span class="flex items-center space-x-1">
                  <Key class="w-3.5 h-3.5 text-amber-400" />
                  <span>管理员密令 (Admin Token)</span>
                </span>
                <span class="text-[10px] text-slate-500">开发默认: starward-secret-token-2026</span>
              </label>
              <div class="relative">
                <input
                  v-model="tokenInput"
                  :type="showPassword ? 'text' : 'password'"
                  placeholder="请输入星轨管理密令..."
                  class="w-full px-4 py-3 rounded-xl bg-space-950/80 border border-white/10 text-slate-100 text-sm placeholder-slate-500 focus:outline-none focus:border-amber-400/80 focus:ring-1 focus:ring-amber-400/50 transition-all font-mono"
                  autofocus
                />
                <button
                  type="button"
                  @click="showPassword = !showPassword"
                  class="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-200 transition-colors"
                >
                  <Eye v-if="!showPassword" class="w-4 h-4" />
                  <EyeOff v-else class="w-4 h-4" />
                </button>
              </div>
            </div>

            <!-- 错误提示 -->
            <p v-if="errorMessage" class="text-xs text-rose-400 flex items-center space-x-1 animate-pulse">
              <AlertCircle class="w-3.5 h-3.5 shrink-0" />
              <span>{{ errorMessage }}</span>
            </p>

            <!-- 操作按钮 -->
            <div class="flex items-center space-x-3 pt-2">
              <button
                type="button"
                @click="closeModal"
                class="flex-1 py-2.5 rounded-xl border border-white/10 text-slate-400 hover:text-white hover:bg-white/5 text-sm font-medium transition-all"
              >
                取消
              </button>
              <button
                type="submit"
                :disabled="loading || !tokenInput.trim()"
                class="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-amber-400 via-rose-500 to-nebula-cyan text-white text-sm font-bold shadow-lg shadow-amber-500/25 hover:opacity-90 active:scale-95 disabled:opacity-50 disabled:pointer-events-none transition-all flex items-center justify-center space-x-1.5"
              >
                <Loader2 v-if="loading" class="w-4 h-4 animate-spin" />
                <Sparkles v-else class="w-4 h-4" />
                <span>{{ loading ? '正在校验...' : '验证并跃迁' }}</span>
              </button>
            </div>
          </form>
        </div>
      </div>
    </transition>
  </teleport>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { X, Key, Eye, EyeOff, Sparkles, Loader2, AlertCircle, ShieldCheck } from 'lucide-vue-next';
import confetti from 'canvas-confetti';
import { verifyAdminToken } from '@/api/admin';

const props = defineProps<{
  modelValue: boolean;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void;
}>();

const router = useRouter();
const tokenInput = ref('');
const showPassword = ref(false);
const loading = ref(false);
const errorMessage = ref('');
const shake = ref(false);
const hasExistingToken = ref(false);

watch(() => props.modelValue, (isOpen) => {
  if (isOpen) {
    errorMessage.value = '';
    const stored = localStorage.getItem('starward_admin_token');
    hasExistingToken.value = !!stored;
    if (stored) {
      tokenInput.value = stored;
    }
  }
});

const closeModal = () => {
  emit('update:modelValue', false);
};

const triggerShake = () => {
  shake.value = true;
  setTimeout(() => {
    shake.value = false;
  }, 500);
};

const enterDirectly = () => {
  closeModal();
  router.push('/admin/posts');
};

const handleVerify = async () => {
  const token = tokenInput.value.trim();
  if (!token) return;

  loading.value = true;
  errorMessage.value = '';

  const isValid = await verifyAdminToken(token);
  loading.value = false;

  if (isValid) {
    localStorage.setItem('starward_admin_token', token);
    // 燃放赛博星轨烟花
    confetti({
      particleCount: 80,
      spread: 70,
      origin: { y: 0.6 },
      colors: ['#38bdf8', '#f59e0b', '#ec4899', '#10b981'],
    });

    closeModal();
    router.push('/admin/posts');
  } else {
    triggerShake();
    errorMessage.value = '跃迁密令校验失败：列车长帕姆拒绝了通行申请！';
  }
};
</script>

<style scoped>
@keyframes shake {
  0%, 100% { transform: translateX(0); }
  20%, 60% { transform: translateX(-6px); }
  40%, 80% { transform: translateX(6px); }
}

.animate-shake {
  animation: shake 0.4s ease-in-out;
}
</style>
