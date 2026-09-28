package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.starward.common.Result;
import top.starward.dto.TagCreateRequest;
import top.starward.service.TagService;

import java.util.List;

/**
 * 🌌 标签模块 REST API
 */
@Tag(name = "标签模块", description = "文章标签的查询、新增与维护")
@RestController
@RequestMapping("/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    /**
     * 获取所有标签
     */
    @Operation(summary = "获取所有标签", description = "获取系统全量可用标签列表及其主题色")
    @GetMapping
    public Result<List<top.starward.entity.Tag>> getAllTags() {
        return Result.success(tagService.getAllTags());
    }

    /**
     * 创建新标签
     */
    @Operation(summary = "创建新标签", description = "新增一个分类标签，需 X-Admin-Token 鉴权")
    @PostMapping
    public Result<top.starward.entity.Tag> createTag(@Valid @RequestBody TagCreateRequest request) {
        top.starward.entity.Tag tag = tagService.createTag(request);
        return Result.success("标签创建成功 🏷️", tag);
    }

    /**
     * 删除标签
     */
    @Operation(summary = "删除标签", description = "根据 ID 删除标签并解绑与文章的关联，需 X-Admin-Token 鉴权")
    @DeleteMapping("/{id}")
    public Result<Void> deleteTag(@Parameter(description = "标签主键 ID", example = "1") @PathVariable("id") Long id) {
        tagService.deleteTag(id);
        return Result.success("标签已成功删除 🗑️", null);
    }
}
