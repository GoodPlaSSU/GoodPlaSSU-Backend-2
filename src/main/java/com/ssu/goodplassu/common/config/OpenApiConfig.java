package com.ssu.goodplassu.common.config;

import com.ssu.goodplassu.common.config.auth.dto.SecurityUserDto;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.servlet.*;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

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

	@Bean
	public FilterRegistrationBean<Filter> testSecurityContextFilter() {
		return new FilterRegistrationBean<>(new Filter() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				// 테스트용 SecurityUserDto 객체 생성
				SecurityUserDto testUser = new SecurityUserDto(
						1L,
						"john.doe@example.com",
						"John Doe",
						"portrait_url_here",
						"USER"
				);

				// Authentication 객체 생성
				Authentication authentication = new UsernamePasswordAuthenticationToken(
						testUser, null, List.of(new SimpleGrantedAuthority(testUser.getRole()))
				);

				// SecurityContextHolder에 Authentication 객체 설정
				SecurityContextHolder.getContext().setAuthentication(authentication);

				// 다음 필터로 진행
				chain.doFilter(request, response);
			}
		});
	}
}
