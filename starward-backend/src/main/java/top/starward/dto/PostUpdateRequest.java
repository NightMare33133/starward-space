package top.starward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 🌌 更新文章请求体
 */
@Data
@Schema(description = "更新文章请求体")
public class PostUpdateRequest {

    @Schema(description = "文章标题", example = "Spring Boot 3 与 Vue 3 极简全栈工程落地 (修订版)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 150, message = "文章标题不能超过 150 个字符")
    private String title;

    @Schema(description = "URL 语义化短链 Slug", example = "springboot3-vue3-guide", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "文章 Slug 不能为空")
    @Size(max = 150, message = "Slug 不能超过 150 个字符")
    private String slug;

    @Schema(description = "文章摘要简介", example = "记录从零搭建个人极客空间的技术思考与架构实践。")
    @Size(max = 300, message = "文章摘要不能超过 300 个字符")
    private String summary;

    @Schema(description = "Markdown 源码内容", example = "## 1. 架构总览\n\n以坚实底盘支撑纯粹极客审美...", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Markdown 正文内容不能为空")
    private String contentMd;

    @Schema(description = "文章封面图 URL", example = "https://images.unsplash.com/photo-1506703719100-a0f3a48c0f86")
    private String coverImage;

    @Schema(description = "发布状态: DRAFT (草稿) / PUBLISHED (已发布)", example = "PUBLISHED")
    private String status;

    @Schema(description = "是否置顶: 0-否, 1-是", example = "0")
    private Integer isPinned;

    @Schema(description = "关联的标签 ID 列表", example = "[1, 2, 3]")
    private List<Long> tagIds;
}
