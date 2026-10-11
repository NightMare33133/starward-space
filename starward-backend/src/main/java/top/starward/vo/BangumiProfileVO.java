package top.starward.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 🌸 Bangumi (bgm.tv) 用户个人资料 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BangumiProfileVO {

    private Integer id;
    private String username;
    private String nickname;
    private String sign;
    private String avatar;
    private String url;
}
