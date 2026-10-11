package top.starward.service;

import top.starward.entity.BilibiliBangumi;
import top.starward.vo.BilibiliSummaryVO;

import java.util.List;
import java.util.Map;

/**
 * 📺 哔哩哔哩追番追剧业务接口
 */
public interface BilibiliService {

    List<BilibiliBangumi> getBangumiList(String seasonTypeName, String keyword, String sort);

    BilibiliSummaryVO getSummary();

    Map<String, Object> syncFromBilibili(String vmid);
}
