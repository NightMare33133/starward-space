package top.starward.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.starward.common.Result;
import top.starward.service.PostService;
import top.starward.vo.PostDetailVO;
import top.starward.vo.PostListVO;

import java.util.List;

/**
 * 🌌 文章模块 REST API
 */
@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 获取文章列表
     */
    @GetMapping
    public Result<List<PostListVO>> getPostList() {
        return Result.success(postService.getPublishedPostList());
    }

    /**
     * 根据 ID 获取文章详情
     */
    @GetMapping("/{id}")
    public Result<PostDetailVO> getPostDetail(@PathVariable("id") Long id) {
        return Result.success(postService.getPostDetail(id));
    }

    /**
     * 根据 Slug 获取文章详情
     */
    @GetMapping("/slug/{slug}")
    public Result<PostDetailVO> getPostDetailBySlug(@PathVariable("slug") String slug) {
        return Result.success(postService.getPostDetailBySlug(slug));
    }
}
