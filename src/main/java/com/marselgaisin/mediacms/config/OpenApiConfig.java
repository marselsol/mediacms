package com.marselgaisin.mediacms.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mediaCmsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MediaCMS API")
                        .version("0.0.1")
                        .description("Training API for a CMS, covering content, authentication, and analytics."));
    }
}
