package top.starward.service;

import top.starward.dto.LoginRequest;
import top.starward.vo.LoginResponseVO;
import top.starward.vo.UserVO;

/**
 * 🌌 认证与授权业务接口
 */
public interface AuthService {

    /**
     * 用户账号密码登录，成功返回 JWT 令牌与用户信息
     */
    LoginResponseVO login(LoginRequest request);

    /**
     * 根据 Token 获取当前登录用户信息
     */
    UserVO getCurrentUser(String token);
}
