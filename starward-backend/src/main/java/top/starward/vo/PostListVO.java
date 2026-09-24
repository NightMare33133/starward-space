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
 * 🌌 文章列表视图对象 (不含冗长正文，极大节省网络带宽与内存)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String slug;
    private String summary;
    private String coverImage;
    private Integer isPinned;
    private Integer viewCount;
    private LocalDateTime createdAt;
    private List<Tag> tags;
}
