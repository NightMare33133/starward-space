package top.starward.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.starward.entity.Post;

import java.util.List;

/**
 * 🌌 文章持久层 Mapper
 */
@Mapper
public interface PostMapper {

    /**
     * 查询所有已发布的文章 (置顶优先，最新发布优先)
     */
    List<Post> findPublishedPosts();

    /**
     * 根据主键 ID 查询单篇文章
     */
    Post findPostById(@Param("id") Long id);

    /**
     * 根据 URL Slug 查询单篇文章
     */
    Post findPostBySlug(@Param("slug") String slug);

    /**
     * 阅读量自增原子操作 (保证并发安全: view_count = view_count + 1)
     */
    int incrementViewCount(@Param("id") Long id);
}
