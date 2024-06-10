package com.ssu.goodplassu.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
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
				;
	}
}
