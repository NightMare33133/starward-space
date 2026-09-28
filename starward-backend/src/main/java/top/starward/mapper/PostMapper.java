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

    /**
     * 插入新文章 (自动回填自增 ID 到 post.id)
     */
    int insertPost(Post post);

    /**
     * 更新已有文章
     */
    int updatePost(Post post);

    /**
     * 根据主键 ID 删除单篇文章
     */
    int deletePostById(@Param("id") Long id);

    /**
     * 管理员查询全量文章 (包括草稿与已发布)
     */
    List<Post> findAllPostsForAdmin();

    /**
     * 批量插入文章-标签多对多关联
     */
    int insertPostTags(@Param("postId") Long postId, @Param("tagIds") List<Long> tagIds);

    /**
     * 删除指定文章的所有标签关联
     */
    int deletePostTagsByPostId(@Param("postId") Long postId);
}
