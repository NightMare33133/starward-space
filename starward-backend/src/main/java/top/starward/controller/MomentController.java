package top.starward.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.starward.common.Result;
import top.starward.dto.MomentCreateRequest;
import top.starward.entity.Moment;
import top.starward.service.MomentService;

import java.util.List;

/**
 * 🌌 星际碎语/说说模块 REST API
 */
@RestController
@RequestMapping("/v1/moments")
@RequiredArgsConstructor
public class MomentController {

    private final MomentService momentService;

    /**
     * 获取星际碎语时间线
     */
    @GetMapping
    public Result<List<Moment>> getMomentTimeline() {
        return Result.success(momentService.getMomentTimeline());
    }

    /**
     * 发布一条新碎语 (带 @Valid 参数防御校验)
     */
    @PostMapping
    public Result<Moment> createMoment(@Valid @RequestBody MomentCreateRequest request) {
        Moment moment = momentService.createMoment(request);
        return Result.success("碎语发布成功 ✨", moment);
    }
}
