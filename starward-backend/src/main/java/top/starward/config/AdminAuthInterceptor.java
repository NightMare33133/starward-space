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

import java.nio.charset.StandardCharsets;

/**
 * 🌌 管理端安全鉴权拦截器
 * 拦截所有敏感写入 (POST/PUT/DELETE) 与管理员专有查询 (/admin/**)
 * 校验请求头中的 X-Admin-Token
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    @Value("${starward.auth.admin-token:starward-secret-token-2026}")
    private String configuredAdminToken;

    private final ObjectMapper objectMapper;

    public static final String ADMIN_TOKEN_HEADER = "X-Admin-Token";

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
        String token = request.getHeader(ADMIN_TOKEN_HEADER);

        if (token != null && token.equals(configuredAdminToken)) {
            return true;
        }

        log.warn("🚨 拦截到未授权的操作尝试: [{} {}], 来源 IP: {}", 
                request.getMethod(), request.getRequestURI(), request.getRemoteAddr());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        Result<?> errorResult = Result.error(401, "未授权访问：请在请求头携带有效的 " + ADMIN_TOKEN_HEADER + " 管理员密钥");
        response.getWriter().write(objectMapper.writeValueAsString(errorResult));
        return false;
    }
}
