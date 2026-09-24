import apiClient from './client';
import type { PostListVO, PostDetailVO } from '@/types';

export const getPostList = (): Promise<PostListVO[]> => {
  return apiClient.get('/v1/posts');
};

export const getPostDetail = (id: number): Promise<PostDetailVO> => {
  return apiClient.get(`/v1/posts/${id}`);
};

export const getPostDetailBySlug = (slug: string): Promise<PostDetailVO> => {
  return apiClient.get(`/v1/posts/slug/${slug}`);
};
