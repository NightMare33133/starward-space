package top.starward.service;

import top.starward.vo.PostDetailVO;
import top.starward.vo.PostListVO;

import java.util.List;

/**
 * 🌌 文章业务服务接口
 */
public interface PostService {

    /**
     * 获取所有已发布文章摘要列表 (用于首页/列表展示)
     */
    List<PostListVO> getPublishedPostList();

    /**
     * 根据主键 ID 获取文章详情 (含正文并自增浏览量)
     */
    default PostDetailVO getPostDetail(Long id) {
        return getPostDetail(id, false);
    }

    /**
     * 根据主键 ID 获取文章详情 (支持管理员预览草稿)
     */
    PostDetailVO getPostDetail(Long id, boolean isAdmin);

    /**
     * 根据 Slug 获取文章详情 (含正文并自增浏览量)
     */
    default PostDetailVO getPostDetailBySlug(String slug) {
        return getPostDetailBySlug(slug, false);
    }

    /**
     * 根据 Slug 获取文章详情 (支持管理员预览草稿)
     */
    PostDetailVO getPostDetailBySlug(String slug, boolean isAdmin);

    /**
     * 创建新文章 (事务保证标签关系一致性)
     */
    PostDetailVO createPost(top.starward.dto.PostCreateRequest request);

    /**
     * 更新已有文章
     */
    PostDetailVO updatePost(Long id, top.starward.dto.PostUpdateRequest request);

    /**
     * 删除文章 (级联清理标签关联)
     */
    void deletePost(Long id);

    /**
     * 管理员获取全量文章列表 (含草稿)
     */
    List<PostListVO> getAdminPostList();

    /**
     * 管理员获取指定文章详情 (含草稿免检与正文，不自增浏览量)
     */
    PostDetailVO getAdminPostDetail(Long id);

    /**
     * 快速更新文章发布状态 (PUBLISHED / DRAFT)
     */
    PostDetailVO updatePostStatus(Long id, String status);

    /**
     * 快速更新文章置顶状态 (0 / 1)
     */
    PostDetailVO updatePostPin(Long id, Integer isPinned);
}
