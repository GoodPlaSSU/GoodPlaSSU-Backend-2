package com.ssu.goodplassu.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:3000", "https://goodplassu.netlify.app") // Next.js 애플리케이션의 URL
				.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
				.allowCredentials(true)
				.exposedHeaders(HttpHeaders.LOCATION);
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
}
