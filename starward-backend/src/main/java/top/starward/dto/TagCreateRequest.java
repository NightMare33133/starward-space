package top.starward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 🌌 创建标签请求体
 */
@Data
@Schema(description = "创建标签请求体")
public class TagCreateRequest {

    @Schema(description = "标签名称", example = "Spring Boot 3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称不能超过 50 个字符")
    private String name;

    @Schema(description = "URL 别名 Slug", example = "springboot3", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "标签 Slug 不能为空")
    @Size(max = 50, message = "标签 Slug 不能超过 50 个字符")
    private String slug;

    @Schema(description = "标签主题色十六进制", example = "#10b981")
    @Size(max = 20, message = "主题色不能超过 20 字符")
    private String color = "#6366f1";
}
