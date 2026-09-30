import apiClient from './client';
import type {
  PostListVO,
  PostDetailVO,
  PostCreateRequest,
  PostUpdateRequest,
  Tag,
  TagCreateRequest,
  Moment,
  MomentCreateRequest
} from '@/types';

/**
 * 验证管理员密令是否有效
 */
export const verifyAdminToken = async (token: string): Promise<boolean> => {
  try {
    await apiClient.get('/v1/posts/admin', {
      headers: {
        'X-Admin-Token': token,
      },
    });
    return true;
  } catch (err) {
    return false;
  }
};

/**
 * 管理员获取全量文章列表（含草稿）
 */
export const getAdminPosts = (): Promise<PostListVO[]> => {
  return apiClient.get('/v1/posts/admin');
};

/**
 * 获取文章详情（用于编辑回填，支持草稿）
 */
export const getPostForEdit = (id: number): Promise<PostDetailVO> => {
  return apiClient.get(`/v1/posts/admin/${id}`);
};

/**
 * 快速更新文章发布状态
 */
export const updatePostStatus = (id: number, status: 'PUBLISHED' | 'DRAFT'): Promise<PostDetailVO> => {
  return apiClient.patch(`/v1/posts/admin/${id}/status`, { status });
};

/**
 * 快速更新文章置顶状态
 */
export const updatePostPin = (id: number, isPinned: boolean | number): Promise<PostDetailVO> => {
  return apiClient.patch(`/v1/posts/admin/${id}/pin`, { isPinned: isPinned ? 1 : 0 });
};

/**
 * 创建新文章
 */
export const createPost = (data: PostCreateRequest): Promise<PostDetailVO> => {
  return apiClient.post('/v1/posts', data);
};

/**
 * 更新文章
 */
export const updatePost = (id: number, data: PostUpdateRequest): Promise<PostDetailVO> => {
  return apiClient.put(`/v1/posts/${id}`, data);
};

/**
 * 删除文章
 */
export const deletePost = (id: number): Promise<void> => {
  return apiClient.delete(`/v1/posts/${id}`);
};

/**
 * 获取系统所有标签
 */
export const getAllTags = (): Promise<Tag[]> => {
  return apiClient.get('/v1/tags');
};

/**
 * 创建新标签
 */
export const createTag = (data: TagCreateRequest): Promise<Tag> => {
  return apiClient.post('/v1/tags', data);
};

/**
 * 删除标签
 */
export const deleteTag = (id: number): Promise<void> => {
  return apiClient.delete(`/v1/tags/${id}`);
};

/**
 * 发射星际碎语
 */
export const publishMoment = (data: MomentCreateRequest): Promise<Moment> => {
  return apiClient.post('/v1/moments', data);
};
