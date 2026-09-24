package top.starward.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.starward.entity.Tag;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 🌌 文章详情视图对象 (包含完整 Markdown 内容与标签)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String contentMd;
    private String coverImage;
    private Integer isPinned;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Tag> tags;
}
