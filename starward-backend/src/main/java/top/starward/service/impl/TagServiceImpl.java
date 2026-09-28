package top.starward.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starward.common.ResultCode;
import top.starward.dto.TagCreateRequest;
import top.starward.entity.Tag;
import top.starward.exception.BusinessException;
import top.starward.mapper.TagMapper;
import top.starward.service.TagService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagMapper tagMapper;

    @Override
    public List<Tag> getAllTags() {
        return tagMapper.findAllTags();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tag createTag(TagCreateRequest request) {
        Tag existingSlug = tagMapper.findTagBySlug(request.getSlug());
        if (existingSlug != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "标签 Slug 已存在，请更换");
        }

        Tag tag = Tag.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .color(request.getColor() != null && !request.getColor().isBlank() ? request.getColor() : "#6366f1")
                .build();

        tagMapper.insertTag(tag);
        return tag;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTag(Long id) {
        Tag existing = tagMapper.findTagById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "待删除的标签不存在");
        }
        tagMapper.deletePostTagsByTagId(id);
        tagMapper.deleteTagById(id);
    }
}
