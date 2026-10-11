package top.starward.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starward.entity.BilibiliBangumi;
import top.starward.mapper.BilibiliMapper;
import top.starward.service.BilibiliService;
import top.starward.vo.BilibiliSummaryVO;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 📺 哔哩哔哩追番追剧业务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BilibiliServiceImpl implements BilibiliService {

    private final BilibiliMapper bilibiliMapper;
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_VMID = "28083538";
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public List<BilibiliBangumi> getBangumiList(String seasonTypeName, String keyword, String sort) {
        return bilibiliMapper.selectBangumis(seasonTypeName, keyword, sort);
    }

    @Override
    public BilibiliSummaryVO getSummary() {
        Long total = bilibiliMapper.countAll();
        Double avg = bilibiliMapper.calculateAverageScore();
        List<Map<String, Object>> stats = bilibiliMapper.countByTypeStats();
        LocalDateTime lastUpdated = bilibiliMapper.findLastUpdatedTime();

        String lastUpdatedStr = "-";
        if (lastUpdated != null) {
            lastUpdatedStr = lastUpdated.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"));
        }

        return BilibiliSummaryVO.builder()
                .totalCount(total != null ? total : 0L)
                .avgScore(avg != null ? BigDecimal.valueOf(avg) : BigDecimal.ZERO)
                .typeStats(stats != null ? stats : List.of())
                .lastUpdated(lastUpdatedStr)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> syncFromBilibili(String vmid) {
        String targetVmid = (vmid != null && !vmid.isBlank()) ? vmid.trim() : DEFAULT_VMID;
        log.info("开始从哔哩哔哩同步追番与追剧数据，目标 UID: {}", targetVmid);

        int totalAnime = syncType(targetVmid, 1);
        int totalDrama = syncType(targetVmid, 2);

        Map<String, Object> result = new HashMap<>();
        result.put("vmid", targetVmid);
        result.put("syncedAnime", totalAnime);
        result.put("syncedDrama", totalDrama);
        result.put("totalSynced", totalAnime + totalDrama);
        result.put("syncTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        log.info("哔哩哔哩数据同步完成！番剧: {}, 追剧: {}, 总计: {}", totalAnime, totalDrama, totalAnime + totalDrama);
        return result;
    }

    private int syncType(String vmid, int typeCode) {
        int page = 1;
        int count = 0;

        while (true) {
            String url = String.format("https://api.bilibili.com/x/space/bangumi/follow/list?type=%d&follow_status=0&pn=%d&ps=30&vmid=%s",
                    typeCode, page, vmid);

            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                        .timeout(Duration.ofSeconds(15))
                        .GET()
                        .build();

                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    log.warn("B站接口请求异常，HTTP 状态码: {}", response.statusCode());
                    break;
                }

                JsonNode root = objectMapper.readTree(response.body());
                int code = root.path("code").asInt(-1);
                if (code != 0) {
                    log.warn("B站接口返回业务错误，code: {}, msg: {}", code, root.path("message").asText());
                    break;
                }

                JsonNode dataNode = root.path("data");
                JsonNode listNode = dataNode.path("list");
                if (listNode.isMissingNode() || !listNode.isArray() || listNode.isEmpty()) {
                    break;
                }

                for (JsonNode item : listNode) {
                    BilibiliBangumi b = parseBangumiItem(item, typeCode);
                    if (b != null) {
                        bilibiliMapper.upsert(b);
                        count++;
                    }
                }

                if (listNode.size() < 30) {
                    break;
                }
                page++;
                Thread.sleep(300);
            } catch (Exception e) {
                log.error("同步第 {} 页失败: {}", page, e.getMessage(), e);
                break;
            }
        }
        return count;
    }

    private BilibiliBangumi parseBangumiItem(JsonNode item, int typeCode) {
        long seasonId = item.path("season_id").asLong(0);
        if (seasonId == 0) return null;

        long mediaId = item.path("media_id").asLong(0);
        String title = item.path("title").asText("");
        String seasonTypeName = item.path("season_type_name").asText(typeCode == 1 ? "番剧" : "追剧");
        String cover = item.path("cover").asText("");
        String squareCover = item.path("square_cover").asText("");
        String horizontalCover = item.path("horizontal_cover_16_10").asText("");
        if (horizontalCover.isBlank()) {
            horizontalCover = item.path("horizontal_cover_16_9").asText("");
        }

        int totalCount = item.path("total_count").asInt(0);
        int isFinish = item.path("is_finish").asInt(1);
        String indexShow = item.path("new_ep").path("index_show").asText("");
        double scoreVal = item.path("rating").path("score").asDouble(0.0);
        String evaluate = item.path("evaluate").asText("");
        if (evaluate.isBlank()) {
            evaluate = item.path("summary").asText("");
        }
        String biliUrl = item.path("url").asText("https://www.bilibili.com/bangumi/play/ss" + seasonId);
        String badge = item.path("badge").asText("");

        List<String> areasList = new ArrayList<>();
        JsonNode areasNode = item.path("areas");
        if (areasNode.isArray()) {
            for (JsonNode a : areasNode) {
                areasList.add(a.path("name").asText(""));
            }
        }
        String areas = String.join(",", areasList);

        long viewCount = item.path("stat").path("view").asLong(0);
        long followCount = item.path("stat").path("follow").asLong(0);

        return BilibiliBangumi.builder()
                .mediaId(mediaId)
                .seasonId(seasonId)
                .title(title)
                .type(typeCode)
                .seasonTypeName(seasonTypeName)
                .cover(cover)
                .squareCover(squareCover)
                .horizontalCover(horizontalCover)
                .totalCount(totalCount)
                .isFinish(isFinish)
                .indexShow(indexShow)
                .score(BigDecimal.valueOf(scoreVal))
                .evaluate(evaluate)
                .url(biliUrl)
                .badge(badge)
                .areas(areas)
                .viewCount(viewCount)
                .followCount(followCount)
                .build();
    }
}
