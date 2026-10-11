package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.starward.common.Result;
import top.starward.entity.BilibiliBangumi;
import top.starward.service.BilibiliService;
import top.starward.vo.BilibiliSummaryVO;

import java.util.List;
import java.util.Map;

/**
 * 📺 哔哩哔哩追番放映厅 REST API
 */
@Tag(name = "哔哩哔哩模块", description = "B 站追番追剧归档、统计看板以及数据同步接口")
@RestController
@RequestMapping("/v1/bilibili")
@RequiredArgsConstructor
public class BilibiliController {

    private final BilibiliService bilibiliService;

    @Operation(summary = "获取追番追剧归档列表", description = "支持按类型（番剧/电影/国创等）、关键字模糊搜索以及评分或集数排序")
    @GetMapping
    public Result<List<BilibiliBangumi>> getBangumiList(
            @Parameter(description = "分类名称，如：ALL, 番剧, 国创, 电影, 纪录片", example = "ALL")
            @RequestParam(value = "type", required = false) String type,
            @Parameter(description = "搜索关键词")
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "排序规则：score_desc, count_desc, updated_desc", example = "score_desc")
            @RequestParam(value = "sort", required = false, defaultValue = "score_desc") String sort
    ) {
        return Result.success(bilibiliService.getBangumiList(type, keyword, sort));
    }

    @Operation(summary = "获取追番追剧统计看板", description = "获取总收录数、Bilibili 全站均分、各子分类统计及最新同步时间")
    @GetMapping("/summary")
    public Result<BilibiliSummaryVO> getSummary() {
        return Result.success(bilibiliService.getSummary());
    }

    @Operation(summary = "从哔哩哔哩同步追番数据", description = "根据 B 站 UID 自动全量分页同步追番追剧列表并持久化至 MySQL")
    @PostMapping("/sync")
    public Result<Map<String, Object>> sync(
            @Parameter(description = "B站 UID，默认使用站长 UID: 28083538")
            @RequestParam(value = "vmid", required = false) String vmid
    ) {
        return Result.success(bilibiliService.syncFromBilibili(vmid));
    }
}
