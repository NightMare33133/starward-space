package top.starward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 🌌 发布碎语/说说请求体
 */
@Data
@Schema(description = "发布碎语请求体")
public class MomentCreateRequest {

    @Schema(description = "碎语正文", example = "正在用 Apifox 测接口，梦回苍穹外卖！🚀", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "碎语内容不能为空")
    @Size(max = 1000, message = "碎语内容最多不超过 1000 个字符")
    private String content;

    @Schema(description = "心情 Emoji", example = "🌟")
    @Size(max = 20, message = "心情状态标识不能超过 20 字符")
    private String mood = "✨";

    @Schema(description = "关联的图片链接集合 (JSON 格式或图片 URL)")
    private String imagesJson;
}
