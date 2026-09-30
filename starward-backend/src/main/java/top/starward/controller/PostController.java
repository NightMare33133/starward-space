package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import top.starward.common.Result;
import top.starward.config.AdminAuthInterceptor;
import top.starward.dto.PostCreateRequest;
import top.starward.dto.PostUpdateRequest;
import top.starward.service.PostService;
import top.starward.vo.PostDetailVO;
import top.starward.vo.PostListVO;

import java.util.List;
import java.util.Map;

/**
 * 🌌 文章模块 REST API
 */
@Tag(name = "文章模块", description = "博客文章的查询、阅读量自增与详情获取")
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Value("${starward.auth.admin-token:starward-secret-token-2026}")
    private String configuredAdminToken;

    private boolean isAdminRequest(HttpServletRequest request) {
        if (request == null) return false;
        String token = request.getHeader(AdminAuthInterceptor.ADMIN_TOKEN_HEADER);
        return token != null && token.equals(configuredAdminToken);
    }

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
    @Operation(summary = "根据 ID 获取文章详情", description = "获取完整 Markdown 正文并自动在数据库底层原子递增阅读量（管理员携带 Token 可预览草稿）")
    @GetMapping("/{id}")
    public Result<PostDetailVO> getPostDetail(
            @Parameter(description = "文章唯一主键 ID", example = "1") @PathVariable("id") Long id,
            HttpServletRequest request) {
        return Result.success(postService.getPostDetail(id, isAdminRequest(request)));
    }

    /**
     * 根据 Slug 获取文章详情
     */
    @Operation(summary = "根据 Slug 获取文章详情", description = "根据 SEO 友好语义化短链获取文章详情（管理员携带 Token 可预览草稿）")
    @GetMapping("/slug/{slug}")
    public Result<PostDetailVO> getPostDetailBySlug(
            @Parameter(description = "语义化短链 Slug", example = "my-first-starward-post") @PathVariable("slug") String slug,
            HttpServletRequest request) {
        return Result.success(postService.getPostDetailBySlug(slug, isAdminRequest(request)));
    }

    /**
     * 管理员获取全量文章列表 (包含草稿)
     */
    @Operation(summary = "管理员获取全量文章列表", description = "获取包含 DRAFT (草稿) 与 PUBLISHED (已发布) 的所有文章，需 X-Admin-Token 鉴权")
    @GetMapping("/admin")
    public Result<List<PostListVO>> getAdminPostList() {
        return Result.success(postService.getAdminPostList());
    }

    /**
     * 管理员获取指定文章详情 (包含草稿)
     */
    @Operation(summary = "管理员获取指定文章详情 (包含草稿)", description = "获取草稿或已发布文章完整正文与元数据用于编辑回填，需 X-Admin-Token 鉴权")
    @GetMapping("/admin/{id}")
    public Result<PostDetailVO> getAdminPostDetail(
            @Parameter(description = "文章唯一主键 ID", example = "1") @PathVariable("id") Long id) {
        return Result.success(postService.getAdminPostDetail(id));
    }

    /**
     * 快捷更新文章发布状态
     */
    @Operation(summary = "快捷更新文章发布状态", description = "在 PUBLISHED 与 DRAFT 之间切换，需 X-Admin-Token 鉴权")
    @PatchMapping("/admin/{id}/status")
    public Result<PostDetailVO> updatePostStatus(
            @Parameter(description = "文章唯一主键 ID", example = "1") @PathVariable("id") Long id,
            @RequestBody Map<String, String> body) {
        String status = body != null ? body.get("status") : null;
        PostDetailVO updated = postService.updatePostStatus(id, status);
        return Result.success("文章状态已更新 🌟", updated);
    }

    /**
     * 快捷更新文章置顶状态
     */
    @Operation(summary = "快捷更新文章置顶状态", description = "修改文章置顶属性 (0 或 1)，需 X-Admin-Token 鉴权")
    @PatchMapping("/admin/{id}/pin")
    public Result<PostDetailVO> updatePostPin(
            @Parameter(description = "文章唯一主键 ID", example = "1") @PathVariable("id") Long id,
            @RequestBody Map<String, Object> body) {
        Integer isPinned = 0;
        if (body != null && body.containsKey("isPinned")) {
            Object val = body.get("isPinned");
            if (val instanceof Boolean b) {
                isPinned = b ? 1 : 0;
            } else if (val instanceof Number n) {
                isPinned = n.intValue();
            } else if (val != null) {
                isPinned = "true".equalsIgnoreCase(val.toString()) || "1".equals(val.toString()) ? 1 : 0;
            }
        }
        PostDetailVO updated = postService.updatePostPin(id, isPinned);
        return Result.success("文章置顶状态已更新 📌", updated);
    }

    /**
     * 创建新文章
     */
    @Operation(summary = "创建新文章", description = "发布新博文或保存草稿，需 X-Admin-Token 鉴权，支持关联已有标签")
    @org.springframework.web.bind.annotation.PostMapping
    public Result<PostDetailVO> createPost(@jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody top.starward.dto.PostCreateRequest request) {
        PostDetailVO created = postService.createPost(request);
        return Result.success("文章创建成功 ✨", created);
    }

    /**
     * 更新已有文章
     */
    @Operation(summary = "修改文章", description = "根据主键 ID 更新文章元数据、Markdown 正文及标签关系，需 X-Admin-Token 鉴权")
    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public Result<PostDetailVO> updatePost(
            @Parameter(description = "文章主键 ID", example = "1") @PathVariable("id") Long id,
            @jakarta.validation.Valid @org.springframework.web.bind.annotation.RequestBody top.starward.dto.PostUpdateRequest request) {
        PostDetailVO updated = postService.updatePost(id, request);
        return Result.success("文章更新成功 🚀", updated);
    }

    /**
     * 删除文章
     */
    @Operation(summary = "删除文章", description = "根据主键 ID 彻底删除文章并级联解绑标签，需 X-Admin-Token 鉴权")
    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public Result<Void> deletePost(@Parameter(description = "文章主键 ID", example = "1") @PathVariable("id") Long id) {
        postService.deletePost(id);
        return Result.success("文章已成功删除 🗑️", null);
    }
}
