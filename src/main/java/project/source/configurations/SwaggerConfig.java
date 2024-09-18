package project.source.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {


//    @Bean
//    public OpenAPI openAPI(
//            @Value("${openapi.service.title}") String title,
//            @Value("${openapi.service.version}") String version,
//            @Value("${openapi.service.serverUrl}") String serverUrl,
//            @Value("${openapi.service.serverName}") String serverName) {
//        return new OpenAPI()
//                .servers(List.of(new Server().url(serverUrl).description(serverName)))
//                .info(new Info().title(title)
//                        .description("API documents")
//                        .version(version)
//                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
//    }
}
