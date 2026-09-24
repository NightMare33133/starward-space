package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "星际碎语", description = "动态说说时间线与实时发布")
@RestController
@RequestMapping("/v1/moments")
@RequiredArgsConstructor
public class MomentController {

    private final MomentService momentService;

    /**
     * 获取星际碎语时间线
     */
    @Operation(summary = "获取碎语时间线", description = "按发布时间倒序获取最新的星际碎语动态列表")
    @GetMapping
    public Result<List<Moment>> getMomentTimeline() {
        return Result.success(momentService.getMomentTimeline());
    }

    /**
     * 发布一条新碎语 (带 @Valid 参数防御校验)
     */
    @Operation(summary = "发布星际碎语", description = "发布一条新的说说动态，带 JSR-303 防御性参数校验（正文非空且限长）")
    @PostMapping
    public Result<Moment> createMoment(@Valid @RequestBody MomentCreateRequest request) {
        Moment moment = momentService.createMoment(request);
        return Result.success("碎语发布成功 ✨", moment);
    }
}
