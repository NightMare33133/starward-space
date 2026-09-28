import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import PostsView from '@/views/PostsView.vue';
import PostDetailView from '@/views/PostDetailView.vue';
import MomentsView from '@/views/MomentsView.vue';
import GalleryView from '@/views/GalleryView.vue';
import AboutView from '@/views/AboutView.vue';

import AdminLoginView from '@/views/admin/AdminLoginView.vue';
import AdminLayout from '@/views/admin/AdminLayout.vue';
import AdminPostsView from '@/views/admin/AdminPostsView.vue';
import AdminPostEditorView from '@/views/admin/AdminPostEditorView.vue';
import AdminTagsView from '@/views/admin/AdminTagsView.vue';
import AdminMomentsView from '@/views/admin/AdminMomentsView.vue';

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
      path: '/admin/login',
      name: 'admin-login',
      component: AdminLoginView,
      meta: { title: '列车长密令认证 · Starward Space' }
    },
    {
      path: '/admin',
      component: AdminLayout,
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/admin/posts'
        },
        {
          path: 'posts',
          name: 'admin-posts',
          component: AdminPostsView,
          meta: { title: '文章管理 · 星轨控制中枢', requiresAuth: true }
        },
        {
          path: 'posts/new',
          name: 'admin-post-new',
          component: AdminPostEditorView,
          meta: { title: '撰写星际文章 · 星轨控制中枢', requiresAuth: true }
        },
        {
          path: 'posts/edit/:id',
          name: 'admin-post-edit',
          component: AdminPostEditorView,
          meta: { title: '编辑星际文章 · 星轨控制中枢', requiresAuth: true }
        },
        {
          path: 'tags',
          name: 'admin-tags',
          component: AdminTagsView,
          meta: { title: '标签管理 · 星轨控制中枢', requiresAuth: true }
        },
        {
          path: 'moments',
          name: 'admin-moments',
          component: AdminMomentsView,
          meta: { title: '星际碎语速发 · 星轨控制中枢', requiresAuth: true }
        },
      ]
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

// 全局路由守卫：拦截未授权的 /admin 访问
router.beforeEach((to, _from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    const token = localStorage.getItem('starward_admin_token');
    if (!token) {
      next({
        path: '/admin/login',
        query: { redirect: to.fullPath }
      });
      return;
    }
  }
  next();
});

router.afterEach((to) => {
  if (to.meta.title) {
    document.title = to.meta.title as string;
  }
});

export default router;
