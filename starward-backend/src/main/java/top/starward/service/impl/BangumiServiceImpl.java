package top.starward.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starward.entity.BangumiCollection;
import top.starward.mapper.BangumiMapper;
import top.starward.service.BangumiService;
import top.starward.vo.BangumiProfileVO;
import top.starward.vo.BangumiSummaryVO;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 🌸 Bangumi (bgm.tv) 业务服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BangumiServiceImpl implements BangumiService {

    private final BangumiMapper bangumiMapper;
    private final ObjectMapper objectMapper;

    private static final String DEFAULT_USER_ID = "978388";
    private static final String API_BASE = "https://api.bgm.tv/v0";
    private static final String USER_AGENT = "StarwardSpace/1.0 (https://github.com/NightMare33133/starward-space)";

    private static final Map<Integer, String> SUBJECT_TYPE_MAP = Map.of(
            1, "书籍",
            2, "动画",
            3, "音乐",
            4, "游戏",
            6, "三次元"
    );

    private static final Map<Integer, String> COLLECTION_TYPE_MAP = Map.of(
            1, "想看",
            2, "看过",
            3, "在看",
            4, "搁置",
            5, "抛弃"
    );

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public List<BangumiCollection> getCollectionList(Integer subjectType, Integer collectionType, String keyword, String sort) {
        return bangumiMapper.selectCollections(subjectType, collectionType, keyword, sort);
    }

    @Override
    public BangumiSummaryVO getSummary() {
        Long total = bangumiMapper.countAll();
        Double avgScore = bangumiMapper.calculateAverageScore();
        Double avgUserRate = bangumiMapper.calculateAverageUserRate();
        Long commentCount = bangumiMapper.countWithComment();
        List<Map<String, Object>> typeStats = bangumiMapper.countByTypeStats();
        List<Map<String, Object>> colStats = bangumiMapper.countByCollectionTypeStats();
        LocalDateTime lastUpdated = bangumiMapper.findLastUpdatedTime();

        String lastUpdatedStr = "-";
        if (lastUpdated != null) {
            lastUpdatedStr = lastUpdated.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm"));
        }

        BangumiProfileVO profile = loadProfile();

        return BangumiSummaryVO.builder()
                .totalCount(total != null ? total : 0L)
                .avgScore(avgScore != null ? BigDecimal.valueOf(avgScore).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .avgUserRate(avgUserRate != null ? BigDecimal.valueOf(avgUserRate).setScale(1, RoundingMode.HALF_UP) : BigDecimal.ZERO)
                .commentCount(commentCount != null ? commentCount : 0L)
                .typeStats(typeStats)
                .collectionTypeStats(colStats)
                .profile(profile)
                .lastUpdated(lastUpdatedStr)
                .build();
    }

    private BangumiProfileVO loadProfile() {
        try {
            ClassPathResource resource = new ClassPathResource("bangumi_profile.json");
            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    return objectMapper.readValue(is, BangumiProfileVO.class);
                }
            }
        } catch (Exception e) {
            log.warn("读取 bangumi_profile.json 失败，使用内置默认资料: {}", e.getMessage());
        }

        return BangumiProfileVO.builder()
                .id(978388)
                .username("978388")
                .nickname("NightMare丶明眸")
                .sign("纯主观打分，别骂我QAQ")
                .avatar("https://lain.bgm.tv/pic/user/l/000/97/83/978388.jpg?r=1741865119&hd=1")
                .url("https://bgm.tv/user/978388")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> syncFromBangumi(String userId) {
        String targetUserId = (userId != null && !userId.isBlank()) ? userId.trim() : DEFAULT_USER_ID;
        log.info("开始从 Bangumi (bgm.tv) 同步用户 [{}] 收藏数据...", targetUserId);

        int totalSynced = 0;
        int limit = 50;
        int offset = 0;

        try {
            while (true) {
                String url = String.format("%s/users/%s/collections?limit=%d&offset=%d", API_BASE, targetUserId, limit, offset);
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .timeout(Duration.ofSeconds(15))
                        .header("User-Agent", USER_AGENT)
                        .header("Accept", "application/json")
                        .GET()
                        .build();

                HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() != 200) {
                    log.error("Bangumi API 请求失败 HTTP {}: {}", response.statusCode(), response.body());
                    break;
                }

                JsonNode root = objectMapper.readTree(response.body());
                JsonNode dataNode = root.get("data");
                int total = root.has("total") ? root.get("total").asInt() : 0;

                if (dataNode == null || !dataNode.isArray() || dataNode.isEmpty()) {
                    break;
                }

                for (JsonNode item : dataNode) {
                    BangumiCollection collection = parseCollection(item);
                    if (collection != null) {
                        bangumiMapper.upsert(collection);
                        totalSynced++;
                    }
                }

                if (offset + limit >= total) {
                    break;
                }
                offset += limit;
                Thread.sleep(200); // 礼貌限速
            }

            Map<String, Object> result = new HashMap<>();
            result.put("syncedCount", totalSynced);
            result.put("userId", targetUserId);
            result.put("status", "SUCCESS");
            return result;

        } catch (Exception e) {
            log.error("同步 Bangumi 数据异常", e);
            throw new RuntimeException("同步 Bangumi 失败: " + e.getMessage());
        }
    }

    private BangumiCollection parseCollection(JsonNode item) {
        try {
            JsonNode subject = item.get("subject");
            if (subject == null) {
                return null;
            }

            int subjectId = item.has("subject_id") ? item.get("subject_id").asInt() : subject.path("id").asInt();
            int subjectType = item.has("subject_type") ? item.get("subject_type").asInt() : subject.path("type").asInt(2);
            String subjectTypeName = SUBJECT_TYPE_MAP.getOrDefault(subjectType, "其他");

            String name = subject.path("name").asText("");
            String nameCn = subject.path("name_cn").asText("");
            if (nameCn.isBlank()) {
                nameCn = name;
            }

            JsonNode images = subject.path("images");
            String coverUrl = images.path("large").asText("");
            if (coverUrl.isBlank()) {
                coverUrl = images.path("common").asText("");
            }
            if (coverUrl.isBlank()) {
                coverUrl = images.path("medium").asText("");
            }

            BigDecimal score = BigDecimal.valueOf(subject.path("score").asDouble(0.0)).setScale(1, RoundingMode.HALF_UP);
            int rankNum = subject.path("rank").asInt(0);
            int eps = subject.path("eps").asInt(0);

            int collectionType = item.path("type").asInt(2);
            String collectionTypeName = COLLECTION_TYPE_MAP.getOrDefault(collectionType, "看过");
            int userRate = item.path("rate").asInt(0);
            String userComment = item.path("comment").asText("");
            String airDate = subject.path("date").asText("");

            // 核心标签
            List<String> tagList = new ArrayList<>();
            JsonNode tagsNode = subject.path("tags");
            if (tagsNode.isArray()) {
                for (int i = 0; i < Math.min(5, tagsNode.size()); i++) {
                    String tagName = tagsNode.get(i).path("name").asText("");
                    if (!tagName.isBlank()) {
                        tagList.add(tagName);
                    }
                }
            }
            String tags = String.join(", ", tagList);
            String shortSummary = subject.path("short_summary").asText("");

            LocalDateTime collectionUpdatedAt = null;
            if (item.hasNonNull("updated_at")) {
                try {
                    collectionUpdatedAt = OffsetDateTime.parse(item.get("updated_at").asText()).toLocalDateTime();
                } catch (Exception ignored) {
                }
            }

            return BangumiCollection.builder()
                    .subjectId(subjectId)
                    .subjectType(subjectType)
                    .subjectTypeName(subjectTypeName)
                    .name(name)
                    .nameCn(nameCn)
                    .coverUrl(coverUrl)
                    .score(score)
                    .rankNum(rankNum)
                    .eps(eps)
                    .collectionType(collectionType)
                    .collectionTypeName(collectionTypeName)
                    .userRate(userRate)
                    .userComment(userComment)
                    .airDate(airDate)
                    .tags(tags)
                    .shortSummary(shortSummary)
                    .collectionUpdatedAt(collectionUpdatedAt)
                    .build();
        } catch (Exception e) {
            log.warn("解析 Bangumi 单条数据异常: {}", e.getMessage());
            return null;
        }
    }
}
