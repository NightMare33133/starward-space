package top.starward;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import top.starward.dto.LoginRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("端到端验证：账号密码登录 -> 获取 JWT -> 携带 Bearer 访问 /v1/auth/me 及管理端接口")
    void testFullAuthFlow() throws Exception {
        // 1. 登录成功
        LoginRequest req = new LoginRequest();
        req.setUsername("1052935743");
        req.setPassword("1111817394");

        MvcResult loginResult = mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.data.user.username").value("1052935743"))
                .andExpect(jsonPath("$.data.user.role").value("ADMIN"))
                .andReturn();

        String token = com.jayway.jsonpath.JsonPath.read(loginResult.getResponse().getContentAsString(), "$.data.token");

        // 2. 携带 Bearer JWT 访问 /v1/auth/me
        mockMvc.perform(get("/v1/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.username").value("1052935743"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));

        // 3. 携带 Bearer JWT 访问管理员受保护接口 /v1/posts/admin
        mockMvc.perform(get("/v1/posts/admin")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        // 4. 未携带凭证直接访问受保护管理端接口 -> 401 拦截
        mockMvc.perform(get("/v1/posts/admin"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(401));

        // 5. 错误密码登录 -> 401 拒绝
        LoginRequest wrongReq = new LoginRequest();
        wrongReq.setUsername("1052935743");
        wrongReq.setPassword("wrong-pwd");

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(wrongReq)))
                .andExpect(status().isOk()) // GlobalExceptionHandler 统一包装
                .andExpect(jsonPath("$.code").value(401));
    }
}
