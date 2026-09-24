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
}
