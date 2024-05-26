package com.hopo._config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		Info info = new Info()
			.title("hopo API Documentation")
			.version("v1.0.0")
			.description("hopo API 명세서입니다.");

		SecurityScheme securityScheme = new SecurityScheme()
			.type(SecurityScheme.Type.HTTP).scheme("Bearer").bearerFormat("JWT")
			.in(SecurityScheme.In.HEADER).name("Authorization");
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("Authorization");

		return new OpenAPI()
			.components(new Components().addSecuritySchemes("Authorization", securityScheme))
			.security(List.of(securityRequirement))
			.info(info);
	}
}
