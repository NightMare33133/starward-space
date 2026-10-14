package top.starward.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 🌌 JWT 令牌生成、解析与校验工具组件
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${starward.jwt.secret:starward-space-jwt-super-secret-key-2026}")
    private String secret;

    @Value("${starward.jwt.expire-days:7}")
    private int expireDays;

    private static final String ISSUER = "StarwardSpace";
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ROLE = "role";

    /**
     * 生成 JWT 令牌
     */
    public String generateToken(Long userId, String username, String role) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + (long) expireDays * 24 * 60 * 60 * 1000);

        return JWT.create()
                .withIssuer(ISSUER)
                .withClaim(CLAIM_USER_ID, userId)
                .withClaim(CLAIM_USERNAME, username)
                .withClaim(CLAIM_ROLE, role)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    /**
     * 校验并解码 JWT 令牌
     */
    public DecodedJWT verifyToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            log.debug("JWT 令牌验证失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 检查令牌是否有效且未过期
     */
    public boolean isTokenValid(String token) {
        return verifyToken(token) != null;
    }

    /**
     * 从令牌中提取用户ID
     */
    public Long getUserId(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim(CLAIM_USER_ID).asLong() : null;
    }

    /**
     * 从令牌中提取用户名
     */
    public String getUsername(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim(CLAIM_USERNAME).asString() : null;
    }

    /**
     * 从令牌中提取角色 (如 ADMIN, USER)
     */
    public String getRole(String token) {
        DecodedJWT jwt = verifyToken(token);
        return jwt != null ? jwt.getClaim(CLAIM_ROLE).asString() : null;
    }
}
