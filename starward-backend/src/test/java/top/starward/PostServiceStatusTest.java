package top.starward;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import top.starward.exception.BusinessException;
import top.starward.service.PostService;
import top.starward.vo.PostDetailVO;
import top.starward.vo.PostListVO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostServiceStatusTest {

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("验证后台文章列表正确返回 status 字段")
    void testGetAdminPostListContainsStatus() {
        List<PostListVO> posts = postService.getAdminPostList();
        assertNotNull(posts);
        assertFalse(posts.isEmpty(), "文章列表不应为空");

        // 验证每一篇的 status 均不为 null
        for (PostListVO vo : posts) {
            assertNotNull(vo.getStatus(), "文章 " + vo.getTitle() + " 的 status 不应为 null");
            assertTrue("PUBLISHED".equals(vo.getStatus()) || "DRAFT".equals(vo.getStatus()));
        }
    }

    @Test
    @DisplayName("验证管理员专属详情接口可正常读取草稿文章且普通接口受保护")
    void testAdminDetailAndPublicVisibility() {
        // Post 2 为草稿文章
        Long draftId = 2L;

        // 1. 普通游客访问草稿 -> 抛出 404 异常
        assertThrows(BusinessException.class, () -> {
            postService.getPostDetail(draftId, false);
        }, "普通访客访问草稿应抛出 BusinessException");

        // 2. 管理员带鉴权预览草稿 -> 正常返回
        PostDetailVO previewVO = postService.getPostDetail(draftId, true);
        assertNotNull(previewVO);
        assertEquals("DRAFT", previewVO.getStatus());
        assertNotNull(previewVO.getContentMd());

        // 3. 管理员专属免检详情接口 -> 正常返回
        PostDetailVO adminVO = postService.getAdminPostDetail(draftId);
        assertNotNull(adminVO);
        assertEquals("DRAFT", adminVO.getStatus());
        assertEquals("from-takeaway-to-starward", adminVO.getSlug());
    }

    @Test
    @DisplayName("验证快速切换文章发布状态与置顶状态")
    void testQuickPatchStatusAndPin() {
        Long draftId = 2L;

        // 切换为 PUBLISHED
        PostDetailVO pubVO = postService.updatePostStatus(draftId, "PUBLISHED");
        assertEquals("PUBLISHED", pubVO.getStatus());

        // 再次切回 DRAFT
        PostDetailVO draftVO = postService.updatePostStatus(draftId, "DRAFT");
        assertEquals("DRAFT", draftVO.getStatus());

        // 测试快捷置顶
        PostDetailVO pinnedVO = postService.updatePostPin(draftId, 1);
        assertEquals(1, pinnedVO.getIsPinned());

        PostDetailVO unpinnedVO = postService.updatePostPin(draftId, 0);
        assertEquals(0, unpinnedVO.getIsPinned());
    }
}
