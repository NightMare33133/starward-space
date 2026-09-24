package top.starward.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starward.common.ResultCode;
import top.starward.entity.Post;
import top.starward.entity.Tag;
import top.starward.exception.BusinessException;
import top.starward.mapper.PostMapper;
import top.starward.mapper.TagMapper;
import top.starward.service.PostService;
import top.starward.vo.PostDetailVO;
import top.starward.vo.PostListVO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostMapper postMapper;
    private final TagMapper tagMapper;

    @Override
    public List<PostListVO> getPublishedPostList() {
        List<Post> posts = postMapper.findPublishedPosts();
        List<PostListVO> voList = new ArrayList<>();

        for (Post post : posts) {
            List<Tag> tags = tagMapper.findTagsByPostId(post.getId());
            voList.add(PostListVO.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .slug(post.getSlug())
                    .summary(post.getSummary())
                    .coverImage(post.getCoverImage())
                    .isPinned(post.getIsPinned())
                    .viewCount(post.getViewCount())
                    .createdAt(post.getCreatedAt())
                    .tags(tags)
                    .build());
        }
        return voList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDetailVO getPostDetail(Long id) {
        Post post = postMapper.findPostById(id);
        if (post == null || !"PUBLISHED".equalsIgnoreCase(post.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "该星际漫游文章不存在或已被隐藏");
        }
        // 原子自增浏览量
        postMapper.incrementViewCount(id);

        List<Tag> tags = tagMapper.findTagsByPostId(id);
        return convertToDetailVO(post, tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDetailVO getPostDetailBySlug(String slug) {
        Post post = postMapper.findPostBySlug(slug);
        if (post == null || !"PUBLISHED".equalsIgnoreCase(post.getStatus())) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "目标文章路径不存在");
        }
        postMapper.incrementViewCount(post.getId());

        List<Tag> tags = tagMapper.findTagsByPostId(post.getId());
        return convertToDetailVO(post, tags);
    }

    private PostDetailVO convertToDetailVO(Post post, List<Tag> tags) {
        return PostDetailVO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .summary(post.getSummary())
                .contentMd(post.getContentMd())
                .coverImage(post.getCoverImage())
                .isPinned(post.getIsPinned())
                .viewCount(post.getViewCount() + 1) // 返回自增后的值
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .tags(tags)
                .build();
    }
}
