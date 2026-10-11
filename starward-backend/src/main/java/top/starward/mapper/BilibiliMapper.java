package top.starward.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.starward.entity.BilibiliBangumi;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 📺 哔哩哔哩追番数据持久层 Mapper
 */
@Mapper
public interface BilibiliMapper {

    List<BilibiliBangumi> selectBangumis(
            @Param("seasonTypeName") String seasonTypeName,
            @Param("keyword") String keyword,
            @Param("sort") String sort
    );

    int upsert(BilibiliBangumi bangumi);

    List<Map<String, Object>> countByTypeStats();

    Double calculateAverageScore();

    Long countAll();

    LocalDateTime findLastUpdatedTime();
}
