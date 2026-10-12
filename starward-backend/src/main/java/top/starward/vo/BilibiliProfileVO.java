package top.starward.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 📺 哔哩哔哩用户个人资料 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BilibiliProfileVO {

    private String mid;
    private String name;
    private String face;
    private String sign;
    private Integer level;
    private String pendantImage;
    private String url;
}
