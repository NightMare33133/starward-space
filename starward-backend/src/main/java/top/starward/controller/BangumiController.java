package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.starward.common.Result;
import top.starward.entity.Bangumi;
import top.starward.service.BangumiService;
import top.starward.vo.BangumiSummaryVO;

import java.util.List;
import java.util.Map;

/**
 * 🎬 星穹放映厅 / 追番模块 REST API
 */
@Tag(name = "追番放映厅模块", description = "哔哩哔哩与番剧归档、统计以及数据同步接口")
@RestController
@RequestMapping("/v1/bangumi")
@RequiredArgsConstructor
public class BangumiController {

    private final BangumiService bangumiService;

    @Operation(summary = "获取追番追剧归档列表", description = "支持按类型（番剧/电影/国创等）、关键字模糊搜索以及评分或集数排序")
    @GetMapping
    public Result<List<Bangumi>> getBangumiList(
            @Parameter(description = "分类名称，如：ALL, 番剧, 国创, 电影, 纪录片", example = "ALL")
            @RequestParam(value = "type", required = false) String type,
            @Parameter(description = "搜索关键词")
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "排序规则：score_desc, count_desc, updated_desc", example = "score_desc")
            @RequestParam(value = "sort", required = false, defaultValue = "score_desc") String sort
    ) {
        return Result.success(bangumiService.getBangumiList(type, keyword, sort));
    }

    @Operation(summary = "获取追番追剧统计看板", description = "获取总收录数、Bilibili 全站均分、各子分类统计及最新同步时间")
    @GetMapping("/summary")
    public Result<BangumiSummaryVO> getSummary() {
        return Result.success(bangumiService.getSummary());
    }

    @Operation(summary = "从哔哩哔哩同步追番数据", description = "根据 B 站 UID 自动全量分页同步追番追剧列表并持久化至 MySQL")
    @PostMapping("/sync")
    public Result<Map<String, Object>> sync(
            @Parameter(description = "B站 UID，默认使用站长 UID: 28083538")
            @RequestParam(value = "vmid", required = false) String vmid
    ) {
        return Result.success(bangumiService.syncFromBilibili(vmid));
    }
}
