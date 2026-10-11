package top.starward.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 🌸 Bangumi (bgm.tv) 番组计划收藏条目实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BangumiCollection {

    private Long id;
    private Integer subjectId;
    private Integer subjectType;
    private String subjectTypeName;
    private String name;
    private String nameCn;
    private String coverUrl;
    private BigDecimal score;
    private Integer rankNum;
    private Integer eps;
    private Integer collectionType;
    private String collectionTypeName;
    private Integer userRate;
    private String userComment;
    private String airDate;
    private String tags;
    private String shortSummary;
    private LocalDateTime collectionUpdatedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
