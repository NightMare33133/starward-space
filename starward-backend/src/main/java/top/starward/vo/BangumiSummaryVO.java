package top.starward.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 🎬 追番追剧统计数据 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BangumiSummaryVO {

    private Long totalCount;
    private BigDecimal avgScore;
    private List<Map<String, Object>> typeStats;
    private String lastUpdated;
}
