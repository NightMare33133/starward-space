package top.starward.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.starward.entity.Moment;

import java.util.List;

/**
 * 🌌 星际碎语持久层 Mapper
 */
@Mapper
public interface MomentMapper {

    /**
     * 查询所有碎语时间线 (置顶优先，最新发布优先)
     */
    List<Moment> findAllMoments();

    /**
     * 插入一条新碎语
     */
    int insertMoment(Moment moment);
}
