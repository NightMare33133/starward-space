package top.starward.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.starward.common.ResultCode;
import top.starward.dto.LoginRequest;
import top.starward.entity.User;
import top.starward.exception.BusinessException;
import top.starward.mapper.UserMapper;
import top.starward.service.AuthService;
import top.starward.util.JwtUtils;
import top.starward.vo.LoginResponseVO;
import top.starward.vo.UserVO;

/**
 * 🌌 认证与授权业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    public LoginResponseVO login(LoginRequest request) {
        String username = request.getUsername().trim();
        User user = userMapper.findByUsername(username);

        // 防御：统一返回账号或密码错误，避免用户名探测漏洞
        if (user == null) {
            log.warn("🚨 登录失败：账号不存在 [{}]", username);
            throw new BusinessException(ResultCode.UNAUTHORIZED, "账号或密码错误");
        }

        if (user.getStatus() != null && user.getStatus() != 1) {
            log.warn("🚨 登录拦截：账号已被封禁 [{}]", username);
            throw new BusinessException(ResultCode.FORBIDDEN, "该账号已被冻结，无法登录");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("🚨 登录失败：密码校验不匹配 [{}]", username);
            throw new BusinessException(ResultCode.UNAUTHORIZED, "账号或密码错误");
        }

        // 生成 7 天有效期的 JWT 令牌
        String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
        log.info("✨ 用户登录成功: [{}], 角色: [{}]", user.getUsername(), user.getRole());

        return LoginResponseVO.builder()
                .token(token)
                .tokenType("Bearer")
                .user(convertToVO(user))
                .build();
    }

    @Override
    public UserVO getCurrentUser(String rawToken) {
        String token = normalizeToken(rawToken);
        Long userId = jwtUtils.getUserId(token);
        if (userId == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "登录凭证已失效，请重新登录");
        }

        User user = userMapper.findById(userId);
        if (user == null || (user.getStatus() != null && user.getStatus() != 1)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户不存在或已被禁用");
        }

        return convertToVO(user);
    }

    private String normalizeToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7).trim();
        }
        return token;
    }

    private UserVO convertToVO(User user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .website(user.getWebsite())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
