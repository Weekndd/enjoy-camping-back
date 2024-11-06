package com.ssafy.enjoycamping.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList("X-ACCESS-TOKEN")) // 보안 요구사항 추가
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("X-ACCESS-TOKEN", apiKeySecurityScheme())) // 보안 스키마 정의
                .tags(tags()); // API 태그 설정
    }

    private Info apiInfo() {
        return new Info()
                .title("Enjoy Camping API")
                .description("Enjoy Camping API 명세서")
                .version("v0.0.1");
    }

    private SecurityScheme apiKeySecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .name("X-ACCESS-TOKEN")
                .in(SecurityScheme.In.HEADER);
    }

    private List<Tag> tags() {
        return Arrays.asList(
                new Tag().name("1. TEST").description("테스트 API"),
                new Tag().name("2. USER").description("유저 API"),
                new Tag().name("3. ATTRACTION").description("관광지 API"),
                new Tag().name("* AI").description("AI API")
        );
    }
}
