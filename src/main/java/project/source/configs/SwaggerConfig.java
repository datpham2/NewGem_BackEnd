package project.source.configs;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SwaggerConfig {

    @Bean
    public Map<String, String> apiGroups() {
        Map<String, String> apiGroups = new HashMap<>();
        apiGroups.put("all", "/**");
        apiGroups.put("user", "/user/**");
        apiGroups.put("auth", "/auth/**");
        apiGroups.put("hotel", "/hotel/**");
        apiGroups.put("room", "/room/**");
        apiGroups.put("bill", "/bill/**");
        apiGroups.put("blog", "/blog/**");
        apiGroups.put("gallery", "/gallery/**");
        apiGroups.put("reservation", "/reservation/**");
        apiGroups.put("reviews", "/reviews/**");
        apiGroups.put("voucher", "/voucher/**");
        return apiGroups;
    }

    @Bean
    public List<GroupedOpenApi> apis(Map<String, String> apiGroups) {
        return apiGroups.entrySet().stream()
                .map(entry -> GroupedOpenApi.builder()
                        .group(entry.getKey())
                        .pathsToMatch(entry.getValue())
                        .build())
                .toList();
    }

    @Bean
    public OpenAPI openAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server}") String serverUrl) {
        return new OpenAPI()
                .servers(List.of(new Server().url(serverUrl)))
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .security(List.of(new SecurityRequirement().addList("bearerAuth")))
                .info(new Info().title(title)
                        .description("API documents")
                        .version(version)
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));

    }
}
