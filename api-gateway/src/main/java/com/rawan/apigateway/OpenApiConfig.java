package com.rawan.apigateway;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi userServiceApi() {
        return GroupedOpenApi.builder()
                .group("users")
                .pathsToMatch("/user-service/**")
                .build();
    }

    @Bean
    public GroupedOpenApi postServiceApi() {
        return GroupedOpenApi.builder()
                .group("posts")
                .pathsToMatch("/post-service/**")
                .build();
    }

    @Bean
    public GroupedOpenApi interactionServiceApi() {
        return GroupedOpenApi.builder()
                .group("interactions")
                .pathsToMatch("/interaction-service/**")
                .build();
    }

    @Bean
    public GroupedOpenApi opinionServiceApi() {
        return GroupedOpenApi.builder()
                .group("opinions")
                .pathsToMatch("/opinion-service/**")
                .build();
    }
}
