package com.example.webfluxdemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("响应式微博客 API 文档")
                        .version("1.0.0")
                        .description("这是我们用 Spring WebFlux 和 R2DBC 构建的最终项目 API 文档。")
                );
    }
}