package top.starward.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 🌌 星际碎语/说说实体 (moments 表映射)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Moment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String content;
    private String mood;
    private String imagesJson;
    private Integer isPinned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
