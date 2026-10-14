package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.starward.common.Result;
import top.starward.dto.LoginRequest;
import top.starward.service.AuthService;
import top.starward.vo.LoginResponseVO;
import top.starward.vo.UserVO;

/**
 * 🌌 认证控制层
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "安全认证接口", description = "用户登录认证与当前身份查询")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "用户密码登录", description = "校验账号密码并颁发 JWT 令牌")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前登录用户信息", description = "根据 Authorization 或 X-Admin-Token 请求头解析用户")
    public Result<UserVO> getCurrentUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isBlank()) {
            token = request.getHeader("X-Admin-Token");
        }
        return Result.success(authService.getCurrentUser(token));
    }
}
