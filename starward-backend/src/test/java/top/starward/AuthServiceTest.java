package top.starward;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import top.starward.dto.LoginRequest;
import top.starward.exception.BusinessException;
import top.starward.service.AuthService;
import top.starward.util.JwtUtils;
import top.starward.vo.LoginResponseVO;
import top.starward.vo.UserVO;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("验证 JWT 工具生成与解析")
    void testJwtUtils() {
        String token = jwtUtils.generateToken(1L, "1052935743", "ADMIN");
        assertNotNull(token);
        assertTrue(jwtUtils.isTokenValid(token));
        assertEquals(1L, jwtUtils.getUserId(token));
        assertEquals("1052935743", jwtUtils.getUsername(token));
        assertEquals("ADMIN", jwtUtils.getRole(token));

        // 伪造或非法 Token
        assertFalse(jwtUtils.isTokenValid("invalid.token.here"));
        assertNull(jwtUtils.getUserId("invalid.token.here"));
    }

    @Test
    @DisplayName("验证管理员正常账号密码登录及获取当前信息")
    void testAdminLoginSuccess() {
        LoginRequest req = new LoginRequest();
        req.setUsername("1052935743");
        req.setPassword("1111817394");

        LoginResponseVO resp = authService.login(req);
        assertNotNull(resp);
        assertNotNull(resp.getToken());
        assertEquals("Bearer", resp.getTokenType());
        assertEquals("1052935743", resp.getUser().getUsername());
        assertEquals("ADMIN", resp.getUser().getRole());

        // 使用生成的 token 查询当前用户信息
        UserVO me = authService.getCurrentUser(resp.getToken());
        assertNotNull(me);
        assertEquals("1052935743", me.getUsername());
        assertEquals("ADMIN", me.getRole());
    }

    @Test
    @DisplayName("验证错误密码与未知账号防御拒绝")
    void testLoginFailureDefenses() {
        // 1. 密码错误
        LoginRequest wrongPwdReq = new LoginRequest();
        wrongPwdReq.setUsername("1052935743");
        wrongPwdReq.setPassword("wrong-password");

        BusinessException ex1 = assertThrows(BusinessException.class, () -> {
            authService.login(wrongPwdReq);
        });
        assertEquals(401, ex1.getCode());

        // 2. 账号不存在
        LoginRequest unknownUserReq = new LoginRequest();
        unknownUserReq.setUsername("unknown_hacker");
        unknownUserReq.setPassword("123456");

        BusinessException ex2 = assertThrows(BusinessException.class, () -> {
            authService.login(unknownUserReq);
        });
        assertEquals(401, ex2.getCode());
    }
}
