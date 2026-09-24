package top.starward.service;

import top.starward.dto.MomentCreateRequest;
import top.starward.entity.Moment;

import java.util.List;

/**
 * 🌌 星际碎语业务服务接口
 */
public interface MomentService {

    /**
     * 获取碎语时间线列表
     */
    List<Moment> getMomentTimeline();

    /**
     * 发布一条新碎语
     */
    Moment createMoment(MomentCreateRequest request);
}
