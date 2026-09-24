<template>
  <div class="relative">
    <!-- 顶部极光阅读进度条 -->
    <div class="fixed top-0 left-0 right-0 h-[3px] z-50 pointer-events-none">
      <div
        class="h-full bg-gradient-to-r from-nebula-cyan via-nebula-purple to-nebula-pink transition-all duration-150"
        :style="{ width: `${readingProgress}%` }"
      ></div>
    </div>

    <!-- 正文渲染容器 -->
    <div
      ref="contentRef"
      class="markdown-body max-w-none prose prose-invert select-text"
      v-html="sanitizedHtml"
      @click="handleContainerClick"
    ></div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted, nextTick, watch } from 'vue';
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/atom-one-dark.css';
import DOMPurify from 'dompurify';
import mermaid from 'mermaid';

const props = defineProps<{
  content: string;
}>();

const contentRef = ref<HTMLElement | null>(null);
const readingProgress = ref(0);

// 初始化 Mermaid 图表渲染引擎
mermaid.initialize({
  startOnLoad: false,
  theme: 'dark',
  themeVariables: {
    darkMode: true,
    background: '#0d111a',
    primaryColor: '#38bdf8',
    primaryTextColor: '#f8fafc',
    primaryBorderColor: '#0284c7',
    lineColor: '#94a3b8',
    secondaryColor: '#a855f7',
    tertiaryColor: '#1e293b'
  }
});

// 初始化 MarkdownIt 实例
const md = new MarkdownIt({
  html: true,
  linkify: true,
  typographer: true,
  highlight: (str: string, lang: string) => {
    // 特殊处理 Mermaid 图表代码块
    if (lang && lang.trim().toLowerCase() === 'mermaid') {
      return `<div class="mermaid-block my-6 p-4 rounded-xl bg-space-900/80 border border-white/10 flex justify-center overflow-x-auto" data-mermaid="${encodeURIComponent(str)}">
        <div class="text-xs text-slate-500 font-mono">Loading chart...</div>
      </div>`;
    }

    // 标准代码高亮处理
    let highlighted = '';
    if (lang && hljs.getLanguage(lang)) {
      try {
        highlighted = hljs.highlight(str, { language: lang, ignoreIllegals: true }).value;
      } catch (__) {
        highlighted = md.utils.escapeHtml(str);
      }
    } else {
      highlighted = md.utils.escapeHtml(str);
    }

    // 注入 macOS 风格控制红黄绿三色圆点与一键复制代码按钮
    return `<div class="code-wrapper relative group my-5 rounded-xl overflow-hidden border border-white/10 bg-[#0d111a] shadow-lg">
      <div class="flex items-center justify-between px-4 py-2 bg-white/5 border-b border-white/5 select-none">
        <div class="flex items-center space-x-1.5">
          <span class="w-3 h-3 rounded-full bg-[#ff5f56]/80 inline-block"></span>
          <span class="w-3 h-3 rounded-full bg-[#ffbd2e]/80 inline-block"></span>
          <span class="w-3 h-3 rounded-full bg-[#27c93f]/80 inline-block"></span>
          <span class="text-xs font-mono text-slate-400 ml-2">${lang || 'code'}</span>
        </div>
        <button class="copy-btn text-xs text-slate-400 hover:text-nebula-cyan px-2 py-0.5 rounded bg-white/5 hover:bg-white/10 transition-colors flex items-center space-x-1" data-code="${encodeURIComponent(str)}">
          <span>复制</span>
        </button>
      </div>
      <pre class="!m-0 !p-4 !bg-transparent"><code>${highlighted}</code></pre>
    </div>`;
  }
});

// 计算脱敏并渲染后的 HTML
const sanitizedHtml = computed(() => {
  if (!props.content) return '';
  const rawHtml = md.render(props.content);
  return DOMPurify.sanitize(rawHtml, {
    ADD_TAGS: ['div', 'button', 'span', 'svg', 'path'],
    ADD_ATTR: ['data-code', 'data-mermaid', 'style', 'class']
  });
});

// 异步渲染页面内的 Mermaid 图表
const renderMermaidDiagrams = async () => {
  await nextTick();
  if (!contentRef.value) return;
  const blocks = contentRef.value.querySelectorAll<HTMLElement>('.mermaid-block');

  blocks.forEach(async (block, index) => {
    const rawCode = decodeURIComponent(block.dataset.mermaid || '');
    if (!rawCode) return;
    try {
      const id = `mermaid-svg-${Date.now()}-${index}`;
      const { svg } = await mermaid.render(id, rawCode);
      block.innerHTML = svg;
    } catch (err) {
      console.warn('Mermaid render error:', err);
      block.innerHTML = `<pre class="text-rose-400 text-xs font-mono p-2">${rawCode}</pre>`;
    }
  });
};

// 监听内容变化，重新渲染 Mermaid
watch(() => props.content, () => {
  renderMermaidDiagrams();
});

// 委托处理一键复制代码功能
const handleContainerClick = (e: MouseEvent) => {
  const target = (e.target as HTMLElement).closest('.copy-btn') as HTMLElement | null;
  if (!target) return;
  const rawCode = decodeURIComponent(target.dataset.code || '');
  if (rawCode) {
    navigator.clipboard.writeText(rawCode).then(() => {
      const span = target.querySelector('span');
      if (span) {
        const originalText = span.textContent;
        span.textContent = '已复制 ✓';
        target.classList.add('text-nebula-cyan');
        setTimeout(() => {
          span.textContent = originalText;
          target.classList.remove('text-nebula-cyan');
        }, 2000);
      }
    });
  }
};

// 监听滚动计算阅读进度
const handleScroll = () => {
  const totalHeight = document.documentElement.scrollHeight - window.innerHeight;
  if (totalHeight > 0) {
    readingProgress.value = Math.min(100, Math.max(0, (window.scrollY / totalHeight) * 100));
  }
};

onMounted(() => {
  renderMermaidDiagrams();
  window.addEventListener('scroll', handleScroll);
});

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});
</script>
