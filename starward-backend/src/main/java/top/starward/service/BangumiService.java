package top.starward.service;

import top.starward.entity.BangumiCollection;
import top.starward.vo.BangumiSummaryVO;

import java.util.List;
import java.util.Map;

/**
 * 🌸 Bangumi (bgm.tv) 业务服务接口
 */
public interface BangumiService {

    /**
     * 条件查询 Bangumi 收藏归档列表
     */
    List<BangumiCollection> getCollectionList(Integer subjectType, Integer collectionType, String keyword, String sort);

    /**
     * 获取 Bangumi 统计看板数据及个人资料
     */
    BangumiSummaryVO getSummary();

    /**
     * 从 Bangumi.tv 同步全量收藏条目
     */
    Map<String, Object> syncFromBangumi(String userId);
}
