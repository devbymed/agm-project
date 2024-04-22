package ma.cimr.agmbackend.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @OpenAPIDefinition(info = @Info(title = "API de gestion des AG de la CIMR",
// version = "1.0"), servers = {
// @Server(url = "http://localhost:8080", description = "Local ENV") })
public class OpenApiConfig {

	@Bean
	GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder().group("api-public").pathsToMatch("/auth/**").build();
	}

	@Bean
	GroupedOpenApi managerApi() {
		return GroupedOpenApi.builder().group("api-manager").pathsToMatch("/users/**").build();
	}
}
