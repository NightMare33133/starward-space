package top.starward.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 🌌 登录成功响应 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录成功响应")
public class LoginResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "JWT 认证令牌")
    private String token;

    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType;

    @Schema(description = "当前登录用户信息")
    private UserVO user;
}
