package top.starward.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 📺 哔哩哔哩追番追剧实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BilibiliBangumi {

    private Long id;
    private Long mediaId;
    private Long seasonId;
    private String title;
    private Integer type;
    private String seasonTypeName;
    private String cover;
    private String squareCover;
    private String horizontalCover;
    private Integer totalCount;
    private Integer isFinish;
    private String indexShow;
    private BigDecimal score;
    private String evaluate;
    private String url;
    private String badge;
    private String areas;
    private Long viewCount;
    private Long followCount;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
