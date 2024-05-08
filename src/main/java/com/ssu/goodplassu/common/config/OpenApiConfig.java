package com.ssu.goodplassu.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addServersItem(new Server().url("/"))
				.info(new Info()
						.title("GoodPlaSSU API")
						.version("1.0")
						.description("GoodPlaSSU API with SpringDoc OpenAPI 3"))
				.addSecurityItem(new SecurityRequirement().addList("OAuth2"))
				.components(new io.swagger.v3.oas.models.Components()
						.addSecuritySchemes("OAuth2", new SecurityScheme()
								.type(SecurityScheme.Type.OAUTH2)
								.description("OAuth2 Authentication")
								.flows(new OAuthFlows()
										.authorizationCode(new OAuthFlow()
												.authorizationUrl("https://accounts.google.com/o/oauth2/auth")
												.tokenUrl("https://oauth2.googleapis.com/token")
												.refreshUrl("https://oauth2.googleapis.com/token")
												.scopes(new Scopes().addString("email", "Access to your email address"))
										)
								)
						)
				);
	}
}
