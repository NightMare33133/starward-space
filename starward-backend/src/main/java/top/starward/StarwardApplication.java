package top.starward;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 🌌 Starward Space - 核心主程序入口
 */
@SpringBootApplication
public class StarwardApplication {

    public static void main(String[] args) {
        SpringApplication.run(StarwardApplication.class, args);
        System.out.println("✨ [Starward Space] 星向空间后端引擎已就绪: http://localhost:8080/api/v1/hello");
    }
}
