<template>
  <div class="min-h-screen flex flex-col">
    <!-- 列车长控制中枢顶部导航栏 -->
    <header class="sticky top-0 z-30 bg-space-950/80 backdrop-blur-xl border-b border-white/10 shadow-lg">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 h-16 flex items-center justify-between">
        <!-- 左侧列车长标识 -->
        <div class="flex items-center space-x-3">
          <div class="w-9 h-9 rounded-xl bg-gradient-to-tr from-amber-400 via-nebula-pink to-nebula-cyan p-[1.5px] shadow-lg shadow-amber-500/20 shrink-0">
            <div class="w-full h-full rounded-[10px] overflow-hidden bg-space-950">
              <img
                src="/images/pompom.png"
                alt="Pom-Pom"
                class="w-full h-full object-cover"
              />
            </div>
          </div>
          <div>
            <div class="flex items-center space-x-2">
              <span class="text-sm font-bold tracking-wider text-slate-100">星轨控制中枢</span>
              <span class="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-amber-400/20 text-amber-300 border border-amber-400/30">
                列车长在线
              </span>
            </div>
            <p class="text-[10px] text-slate-400 font-mono">STARWARD CMS · Command Deck</p>
          </div>
        </div>

        <!-- 中间功能标签页导航 -->
        <nav class="hidden md:flex items-center space-x-1 glass-card px-3 py-1.5 rounded-2xl border border-white/10">
          <router-link
            v-for="item in navTabs"
            :key="item.path"
            :to="item.path"
            class="px-3.5 py-1.5 rounded-xl text-xs font-medium transition-all duration-200 flex items-center space-x-1.5"
            :class="isActiveTab(item.path) ? 'text-white bg-gradient-to-r from-amber-500/30 to-nebula-cyan/30 border border-amber-400/40 shadow-sm' : 'text-slate-400 hover:text-slate-100 hover:bg-white/5'"
          >
            <component :is="item.icon" class="w-3.5 h-3.5 text-amber-400" />
            <span>{{ item.title }}</span>
          </router-link>
        </nav>

        <!-- 右侧返回前台与注销 -->
        <div class="flex items-center space-x-2">
          <router-link
            to="/"
            class="px-3 py-1.5 rounded-xl border border-white/10 hover:border-nebula-cyan/40 bg-white/5 hover:bg-white/10 text-xs text-slate-300 hover:text-white transition-all flex items-center space-x-1.5"
            title="返回星向空间前台"
          >
            <ExternalLink class="w-3.5 h-3.5 text-nebula-cyan" />
            <span class="hidden sm:inline">浏览前台</span>
          </router-link>

          <button
            @click="handleLogout"
            class="px-3 py-1.5 rounded-xl border border-rose-500/20 hover:border-rose-500/40 bg-rose-500/10 hover:bg-rose-500/20 text-xs text-rose-300 transition-all flex items-center space-x-1.5"
            title="退出列车长中枢"
          >
            <LogOut class="w-3.5 h-3.5" />
            <span class="hidden sm:inline">注销密令</span>
          </button>
        </div>
      </div>

      <!-- 移动端二级导航条 -->
      <div class="md:hidden flex items-center justify-around border-t border-white/5 py-2 px-2 bg-space-950/60 overflow-x-auto">
        <router-link
          v-for="item in navTabs"
          :key="item.path"
          :to="item.path"
          class="px-3 py-1 rounded-lg text-xs font-medium shrink-0 flex items-center space-x-1"
          :class="isActiveTab(item.path) ? 'text-amber-300 bg-amber-500/20' : 'text-slate-400'"
        >
          <component :is="item.icon" class="w-3 h-3" />
          <span>{{ item.title }}</span>
        </router-link>
      </div>
    </header>

    <!-- 子页面内容视图 -->
    <main class="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 py-6">
      <router-view v-slot="{ Component }">
        <transition
          enter-active-class="transition duration-200 ease-out"
          enter-from-class="opacity-0 translate-y-2"
          enter-to-class="opacity-100 translate-y-0"
          leave-active-class="transition duration-150 ease-in"
          leave-from-class="opacity-100 translate-y-0"
          leave-to-class="opacity-0 -translate-y-2"
          mode="out-in"
        >
          <component :is="Component" />
        </transition>
      </router-view>
    </main>
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from 'vue-router';
import {
  FileText,
  PenTool,
  Tag as TagIcon,
  MessageSquare,
  ExternalLink,
  LogOut
} from 'lucide-vue-next';

const route = useRoute();
const router = useRouter();

const navTabs = [
  { title: '文章管理', path: '/admin/posts', icon: FileText },
  { title: '新建文章', path: '/admin/posts/new', icon: PenTool },
  { title: '标签管理', path: '/admin/tags', icon: TagIcon },
  { title: '星际碎语', path: '/admin/moments', icon: MessageSquare },
];

const isActiveTab = (path: string) => {
  if (path === '/admin/posts') {
    return route.path === '/admin/posts' || route.path.startsWith('/admin/posts/edit');
  }
  return route.path === path;
};

const handleLogout = () => {
  if (confirm('确认注销当前列车长密令凭证？')) {
    localStorage.removeItem('starward_admin_token');
    router.push('/');
  }
};
</script>
