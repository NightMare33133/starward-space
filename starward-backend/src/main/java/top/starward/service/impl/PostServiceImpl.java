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
        post.setViewCount(post.getViewCount() + 1);

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
        post.setViewCount(post.getViewCount() + 1);

        List<Tag> tags = tagMapper.findTagsByPostId(post.getId());
        return convertToDetailVO(post, tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDetailVO createPost(top.starward.dto.PostCreateRequest request) {
        // Slug 唯一性防重校验
        Post existingSlug = postMapper.findPostBySlug(request.getSlug());
        if (existingSlug != null) {
            throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "文章 Slug 已存在，请更换唯一标识");
        }

        Post post = Post.builder()
                .title(request.getTitle())
                .slug(request.getSlug())
                .summary(request.getSummary())
                .contentMd(request.getContentMd())
                .coverImage(request.getCoverImage())
                .status(request.getStatus() != null ? request.getStatus().toUpperCase() : "PUBLISHED")
                .isPinned(request.getIsPinned() != null ? request.getIsPinned() : 0)
                .viewCount(0)
                .build();

        postMapper.insertPost(post);

        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            postMapper.insertPostTags(post.getId(), request.getTagIds());
        }

        List<Tag> tags = tagMapper.findTagsByPostId(post.getId());
        return convertToDetailVO(post, tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PostDetailVO updatePost(Long id, top.starward.dto.PostUpdateRequest request) {
        Post existing = postMapper.findPostById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "待更新的文章不存在");
        }

        // 如果修改了 Slug，检查新 Slug 是否与其他文章冲突
        if (!existing.getSlug().equalsIgnoreCase(request.getSlug())) {
            Post slugPost = postMapper.findPostBySlug(request.getSlug());
            if (slugPost != null && !slugPost.getId().equals(id)) {
                throw new BusinessException(ResultCode.BAD_REQUEST.getCode(), "新文章 Slug 已被其他文章占用");
            }
        }

        Post postToUpdate = Post.builder()
                .id(id)
                .title(request.getTitle())
                .slug(request.getSlug())
                .summary(request.getSummary())
                .contentMd(request.getContentMd())
                .coverImage(request.getCoverImage())
                .status(request.getStatus() != null ? request.getStatus().toUpperCase() : existing.getStatus())
                .isPinned(request.getIsPinned() != null ? request.getIsPinned() : existing.getIsPinned())
                .build();

        postMapper.updatePost(postToUpdate);

        // 标签关联全量同步
        if (request.getTagIds() != null) {
            postMapper.deletePostTagsByPostId(id);
            if (!request.getTagIds().isEmpty()) {
                postMapper.insertPostTags(id, request.getTagIds());
            }
        }

        Post updatedPost = postMapper.findPostById(id);
        List<Tag> tags = tagMapper.findTagsByPostId(id);
        return convertToDetailVO(updatedPost, tags);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePost(Long id) {
        Post existing = postMapper.findPostById(id);
        if (existing == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "待删除的文章不存在");
        }
        postMapper.deletePostTagsByPostId(id);
        postMapper.deletePostById(id);
    }

    @Override
    public List<PostListVO> getAdminPostList() {
        List<Post> posts = postMapper.findAllPostsForAdmin();
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

    private PostDetailVO convertToDetailVO(Post post, List<Tag> tags) {
        return PostDetailVO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .slug(post.getSlug())
                .summary(post.getSummary())
                .contentMd(post.getContentMd())
                .coverImage(post.getCoverImage())
                .isPinned(post.getIsPinned())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .tags(tags)
                .build();
    }
}
