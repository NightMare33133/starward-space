package top.starward.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.starward.entity.Tag;

import java.util.List;

/**
 * 🌌 标签持久层 Mapper
 */
@Mapper
public interface TagMapper {

    List<Tag> findTagsByPostId(@Param("postId") Long postId);

    List<Tag> findAllTags();

    Tag findTagById(@Param("id") Long id);

    Tag findTagBySlug(@Param("slug") String slug);

    int insertTag(Tag tag);

    int deleteTagById(@Param("id") Long id);

    int deletePostTagsByTagId(@Param("tagId") Long tagId);
}
