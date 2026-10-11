package top.starward.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import top.starward.common.Result;
import top.starward.entity.BangumiCollection;
import top.starward.service.BangumiService;
import top.starward.vo.BangumiSummaryVO;

import java.util.List;
import java.util.Map;

/**
 * 🌸 Bangumi (bgm.tv) 番组计划 REST API
 */
@Tag(name = "Bangumi番组计划模块", description = "bgm.tv 动画/游戏评分标记、个人主观吐槽、全站排行榜与数据同步接口")
@RestController
@RequestMapping("/v1/bangumi")
@RequiredArgsConstructor
public class BangumiController {

    private final BangumiService bangumiService;

    @Operation(summary = "获取 Bangumi 收藏条目列表", description = "支持按条目类型（动画/游戏等）、收藏状态（在看/看过/想看等）、关键词模糊搜索以及多种排序规则")
    @GetMapping
    public Result<List<BangumiCollection>> getCollectionList(
            @Parameter(description = "条目类型：1:书籍, 2:动画, 3:音乐, 4:游戏, 6:三次元", example = "2")
            @RequestParam(value = "subjectType", required = false) Integer subjectType,
            @Parameter(description = "收藏状态：1:想看, 2:看过, 3:在看, 4:搁置, 5:抛弃", example = "2")
            @RequestParam(value = "collectionType", required = false) Integer collectionType,
            @Parameter(description = "搜索关键词")
            @RequestParam(value = "keyword", required = false) String keyword,
            @Parameter(description = "排序规则：user_rate_desc, score_desc, rank_asc, air_date_desc, updated_desc", example = "user_rate_desc")
            @RequestParam(value = "sort", required = false, defaultValue = "user_rate_desc") String sort
    ) {
        return Result.success(bangumiService.getCollectionList(subjectType, collectionType, keyword, sort));
    }

    @Operation(summary = "获取 Bangumi 统计看板与个人资料", description = "获取总标记数、全站评分均分、用户主观评分均分、有评价条目数、分类统计及用户 Profile")
    @GetMapping("/summary")
    public Result<BangumiSummaryVO> getSummary() {
        return Result.success(bangumiService.getSummary());
    }

    @Operation(summary = "从 Bangumi.tv 同步用户收藏数据", description = "根据 Bangumi UID 全量分页拉取收藏数据并更新入库")
    @PostMapping("/sync")
    public Result<Map<String, Object>> sync(
            @Parameter(description = "Bangumi UID，默认站长: 978388")
            @RequestParam(value = "userId", required = false) String userId
    ) {
        return Result.success(bangumiService.syncFromBangumi(userId));
    }
}
