import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import PostsView from '@/views/PostsView.vue';
import PostDetailView from '@/views/PostDetailView.vue';
import MomentsView from '@/views/MomentsView.vue';
import GalleryView from '@/views/GalleryView.vue';
import AboutView from '@/views/AboutView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { title: '首页 · Starward Space' }
    },
    {
      path: '/posts',
      name: 'posts',
      component: PostsView,
      meta: { title: '文章 · Starward Space' }
    },
    {
      path: '/posts/:id',
      name: 'post-detail',
      component: PostDetailView,
      meta: { title: '阅读文章 · Starward Space' }
    },
    {
      path: '/moments',
      name: 'moments',
      component: MomentsView,
      meta: { title: '星际碎语 · Starward Space' }
    },
    {
      path: '/gallery',
      name: 'gallery',
      component: GalleryView,
      meta: { title: '摄影视界 · Starward Space' }
    },
    {
      path: '/about',
      name: 'about',
      component: AboutView,
      meta: { title: '关于我 · Starward Space' }
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/'
    }
  ],
  scrollBehavior() {
    return { top: 0, behavior: 'smooth' };
  }
});

router.afterEach((to) => {
  if (to.meta.title) {
    document.title = to.meta.title as string;
  }
});

export default router;
