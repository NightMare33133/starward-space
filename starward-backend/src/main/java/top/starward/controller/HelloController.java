package top.starward.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.starward.common.Result;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 🌌 星光探针与服务健康检测控制器
 */
@RestController
@RequestMapping("/v1")
public class HelloController {

    @GetMapping("/hello")
    public Result<Map<String, Object>> hello() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("project", "Starward Space (星向空间)");
        info.put("version", "0.0.1-alpha");
        info.put("status", "Online ✨");
        info.put("message", "欢迎来到星向空间！第一缕星光已被成功点亮 🌟");
        info.put("architecture", "Spring Boot 3 + Java 21 LTS");
        return Result.success("星向空间服务连接正常", info);
    }
}
