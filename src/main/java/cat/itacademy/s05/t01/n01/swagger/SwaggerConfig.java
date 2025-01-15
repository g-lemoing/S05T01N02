package cat.itacademy.s05.t01.n01.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi api(){
        return GroupedOpenApi.builder()
                .group("webflux-api")
                .pathsToMatch("/game/**", "/player/**", "/ranking")
                .build();
    }
}
