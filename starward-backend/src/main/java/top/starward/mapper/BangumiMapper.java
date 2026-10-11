package top.starward.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.starward.entity.BangumiCollection;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 🌸 Bangumi 数据持久层 Mapper
 */
@Mapper
public interface BangumiMapper {

    List<BangumiCollection> selectCollections(
            @Param("subjectType") Integer subjectType,
            @Param("collectionType") Integer collectionType,
            @Param("keyword") String keyword,
            @Param("sort") String sort
    );

    int upsert(BangumiCollection collection);

    List<Map<String, Object>> countByTypeStats();

    List<Map<String, Object>> countByCollectionTypeStats();

    Double calculateAverageScore();

    Double calculateAverageUserRate();

    Long countWithComment();

    Long countAll();

    LocalDateTime findLastUpdatedTime();
}
