package top.starward.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.starward.common.Result;
import top.starward.util.JwtUtils;

import java.nio.charset.StandardCharsets;

/**
 * 🌌 管理端安全鉴权拦截器
 * 拦截所有敏感写入 (POST/PUT/DELETE) 与管理员专有查询 (/admin/**)
 * 支持 Authorization (Bearer JWT) 与 X-Admin-Token
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Value("${starward.auth.admin-token:starward-secret-token-2026}")
    private String configuredAdminToken;

    private final ObjectMapper objectMapper;
    private final JwtUtils jwtUtils;

    public static final String ADMIN_TOKEN_HEADER = "X-Admin-Token";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();

        // 1. 放行 OPTIONS 跨域预检请求
        if ("OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // 2. 对 GET 请求：默认放行开放查询，但管理员专属接口 (/admin) 必须校验
        if ("GET".equalsIgnoreCase(method)) {
            if (request.getRequestURI().contains("/admin")) {
                return validateToken(request, response);
            }
            return true;
        }

        // 3. 对所有写入类请求 (POST, PUT, DELETE, PATCH) 严格鉴权
        return validateToken(request, response);
    }

    private boolean validateToken(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String token = resolveToken(request);

        // 1. 静态备用应急密钥支持（向后兼容）
        if (token != null && configuredAdminToken != null && !configuredAdminToken.isBlank() && token.equals(configuredAdminToken)) {
            return true;
        }

        // 2. JWT 令牌解析与 ADMIN 角色权限校验
        if (token != null && jwtUtils.isTokenValid(token)) {
            String role = jwtUtils.getRole(token);
            if ("ADMIN".equalsIgnoreCase(role)) {
                return true;
            } else {
                log.warn("🚨 拦截到非管理员角色操作尝试: [{} {}], 角色: {}",
                        request.getMethod(), request.getRequestURI(), role);
                writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "权限不足：该操作仅限管理员执行");
                return false;
            }
        }

        log.warn("🚨 拦截到未授权的操作尝试: [{} {}], 来源 IP: {}", 
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "未授权访问：请先登录管理员账号或携带有效的通行凭据");
        return false;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION_HEADER);
        if (bearer != null && bearer.startsWith(BEARER_PREFIX)) {
            return bearer.substring(BEARER_PREFIX.length()).trim();
        }
        String xAdminToken = request.getHeader(ADMIN_TOKEN_HEADER);
        if (xAdminToken != null && !xAdminToken.isBlank()) {
            return xAdminToken.trim();
        }
        if (bearer != null && !bearer.isBlank()) {
            return bearer.trim();
        }
        return null;
    }

    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Result<?> errorResult = Result.error(status, message);
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
    }
}
