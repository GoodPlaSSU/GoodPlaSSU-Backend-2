package com.ssu.goodplassu.common.config;

import com.ssu.goodplassu.login.jwt.filter.JwtAuthFilter;
import com.ssu.goodplassu.login.jwt.filter.JwtExceptionFilter;
import com.ssu.goodplassu.member.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final JwtAuthFilter jwtAuthFilter;
	private final JwtExceptionFilter jwtExceptionFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		http
				.httpBasic(h -> h.disable())	// HTTP 기본 인증을 사용하지 않고, 보다 안전한 OAuth2 인증 방식을 사용하도록 설정
				.cors(c -> c.configure(http))	// CORS (Cross-Origin Resource Sharing) 설정 활성화
				.csrf(c -> c.disable())			// REST API를 보호하는 데 자주 사용되는 설정으로, 상태를 유지하지 않는 API 서버에서는 CSRF 보호를 비활성화하는 것이 일반적임
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	// 세션을 사용하지 않고, 각 요청을 독립적으로 인증하는 방식을 선택. JWT 사용 시 필수적인 설정. 스프링 시큐리티가 세션을 생성하지 않고, 있어도 사용하지 않겠다.
				.authorizeHttpRequests(a -> a	// 요청에 대한 접근 제어 설정
								.requestMatchers(new AntPathRequestMatcher("/api/boards/**", HttpMethod.POST.name())).hasRole(Role.USER.name())
								.requestMatchers(new AntPathRequestMatcher("/api/boards/**", HttpMethod.DELETE.name())).hasRole(Role.USER.name())
								.requestMatchers("/openapi/comments/**").hasRole(Role.USER.name())
								.requestMatchers("/openapi/cheers/**").hasRole(Role.USER.name())
								.requestMatchers(
								new MvcRequestMatcher(introspector, "/h2-console/**"),
								new AntPathRequestMatcher("/**")
						).permitAll()
						.anyRequest().authenticated()
				)
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class)
		;

		return http.build();
	}
}
