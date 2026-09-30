package top.starward;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerStatusTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String ADMIN_TOKEN = "starward-secret-token-2026";

    @Test
    @DisplayName("GET /v1/posts/admin 必须返回包含 status 的列表")
    void testGetAdminPosts() throws Exception {
        mockMvc.perform(get("/v1/posts/admin")
                        .header("X-Admin-Token", ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].status").exists());
    }

    @Test
    @DisplayName("GET /v1/posts/admin/{id} 管理员专属详情接口免检读取草稿")
    void testGetAdminPostDetail() throws Exception {
        mockMvc.perform(get("/v1/posts/admin/2")
                        .header("X-Admin-Token", ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("DRAFT"))
                .andExpect(jsonPath("$.data.contentMd").isNotEmpty());
    }

    @Test
    @DisplayName("GET /v1/posts/{id} 普通访客访问草稿报 404，管理员携带 Token 允许前台预览")
    void testPublicPostDetailDraftProtection() throws Exception {
        // 1. 无 Token 访客访问草稿 -> 404
        mockMvc.perform(get("/v1/posts/2"))
                .andExpect(status().isOk()) // GlobalExceptionHandler 统一包装成 Result
                .andExpect(jsonPath("$.code").value(404));

        // 2. 携带 X-Admin-Token 访问草稿 -> 200 成功
        mockMvc.perform(get("/v1/posts/2")
                        .header("X-Admin-Token", ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("DRAFT"));
    }

    @Test
    @DisplayName("PATCH /v1/posts/admin/{id}/status 快速切换状态")
    void testPatchPostStatus() throws Exception {
        mockMvc.perform(patch("/v1/posts/admin/2/status")
                        .header("X-Admin-Token", ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\": \"PUBLISHED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("PUBLISHED"));
    }
}
