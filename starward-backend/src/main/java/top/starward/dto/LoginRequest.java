package top.starward.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 🌌 用户登录请求 DTO
 */
@Data
@Schema(description = "用户登录请求体")
public class LoginRequest {

    @NotBlank(message = "登录账号不能为空")
    @Schema(description = "登录用户名/账号", example = "starlight")
    private String username;

    @NotBlank(message = "登录密码不能为空")
    @Schema(description = "登录密码", example = "starward-secret-2026")
    private String password;
}
