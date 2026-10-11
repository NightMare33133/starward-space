package top.starward.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 🌸 Bangumi (bgm.tv) 统计看板 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BangumiSummaryVO {

    private Long totalCount;
    private BigDecimal avgScore;
    private BigDecimal avgUserRate;
    private Long commentCount;
    private List<Map<String, Object>> typeStats;
    private List<Map<String, Object>> collectionTypeStats;
    private BangumiProfileVO profile;
    private String lastUpdated;
}
