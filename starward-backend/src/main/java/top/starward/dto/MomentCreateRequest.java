package top.starward.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 🌌 发布碎语/说说请求体
 */
@Data
public class MomentCreateRequest {

    @NotBlank(message = "碎语内容不能为空")
    @Size(max = 1000, message = "碎语内容最多不超过 1000 个字符")
    private String content;

    @Size(max = 20, message = "心情状态标识不能超过 20 字符")
    private String mood = "✨";

    /**
     * 关联的图片链接集合 (JSON 或逗号隔开)
     */
    private String imagesJson;
}
