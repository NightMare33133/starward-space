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
    PostDetailVO getPostDetail(Long id);

    /**
     * 根据 Slug 获取文章详情 (含正文并自增浏览量)
     */
    PostDetailVO getPostDetailBySlug(String slug);

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
}
