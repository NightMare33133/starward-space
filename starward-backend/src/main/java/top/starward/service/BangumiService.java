package top.starward.service;

import top.starward.entity.Bangumi;
import top.starward.vo.BangumiSummaryVO;

import java.util.List;
import java.util.Map;

/**
 * 🎬 追番追剧业务接口
 */
public interface BangumiService {

    List<Bangumi> getBangumiList(String seasonTypeName, String keyword, String sort);

    BangumiSummaryVO getSummary();

    Map<String, Object> syncFromBilibili(String vmid);
}
