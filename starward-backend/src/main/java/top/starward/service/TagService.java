package top.starward.service;

import top.starward.dto.TagCreateRequest;
import top.starward.entity.Tag;

import java.util.List;

/**
 * 🌌 标签业务服务接口
 */
public interface TagService {

    /**
     * 获取所有标签
     */
    List<Tag> getAllTags();

    /**
     * 创建新标签
     */
    Tag createTag(TagCreateRequest request);

    /**
     * 删除标签 (并级联解绑关联)
     */
    void deleteTag(Long id);
}
