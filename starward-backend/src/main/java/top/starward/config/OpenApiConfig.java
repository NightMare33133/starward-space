package top.starward.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 3.0 (Swagger) 接口规范配置
 * 支持与 Apifox / Postman / Swagger UI 无缝协同
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI starwardOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Starward Space - 星向空间 API 规范")
                        .description("基于 Spring Boot 3 与 Java 21 LTS 驱动的个人全栈知识空间与数字分身核心服务")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("NightMare33133")
                                .url("https://github.com/NightMare33133/starward-space"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
