package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.starward.common.Result;
import top.starward.service.PostService;
import top.starward.vo.PostDetailVO;
import top.starward.vo.PostListVO;

import java.util.List;

/**
 * 🌌 文章模块 REST API
 */
@Tag(name = "文章模块", description = "博客文章的查询、阅读量自增与详情获取")
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 获取文章列表
     */
    @Operation(summary = "获取已发布文章列表", description = "按发布时间降序获取文章简要信息与标签列表，不包含大字段正文以保证轻量传输")
    @GetMapping
    public Result<List<PostListVO>> getPostList() {
        return Result.success(postService.getPublishedPostList());
    }

    /**
     * 根据 ID 获取文章详情
     */
    @Operation(summary = "根据 ID 获取文章详情", description = "获取完整 Markdown 正文并自动在数据库底层原子递增阅读量")
    @GetMapping("/{id}")
    public Result<PostDetailVO> getPostDetail(@Parameter(description = "文章唯一主键 ID", example = "1") @PathVariable("id") Long id) {
        return Result.success(postService.getPostDetail(id));
    }

    /**
     * 根据 Slug 获取文章详情
     */
    @Operation(summary = "根据 Slug 获取文章详情", description = "根据 SEO 友好语义化短链获取文章详情")
    @GetMapping("/slug/{slug}")
    public Result<PostDetailVO> getPostDetailBySlug(@Parameter(description = "语义化短链 Slug", example = "my-first-starward-post") @PathVariable("slug") String slug) {
        return Result.success(postService.getPostDetailBySlug(slug));
    }
}
