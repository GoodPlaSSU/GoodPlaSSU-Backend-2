package com.ssu.goodplassu.common.config;

import com.ssu.goodplassu.common.config.auth.handler.MyAuthenticationFailureHandler;
import com.ssu.goodplassu.common.config.auth.handler.MyAuthenticationSuccessHandler;
import com.ssu.goodplassu.common.config.auth.jwt.filter.JwtAuthFilter;
import com.ssu.goodplassu.common.config.auth.jwt.filter.JwtExceptionFilter;
import com.ssu.goodplassu.common.config.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;
	private final JwtAuthFilter jwtAuthFilter;
	private final JwtExceptionFilter jwtExceptionFilter;
	private final MyAuthenticationSuccessHandler oAuth2LoginSuccessHandler;
	private final MyAuthenticationFailureHandler oAuth2LoginFailureHandler;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.httpBasic(h -> h.disable())	// HTTP 기본 인증을 사용하지 않고, 보다 안전한 OAuth2 인증 방식을 사용하도록 설정
				.cors(c -> c.configure(http))	// CORS (Cross-Origin Resource Sharing) 설정 활성화
				.csrf(c -> c.disable())			// REST API를 보호하는 데 자주 사용되는 설정으로, 상태를 유지하지 않는 API 서버에서는 CSRF 보호를 비활성화하는 것이 일반적임
				.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))	// 세션을 사용하지 않고, 각 요청을 독립적으로 인증하는 방식을 선택. JWT 사용 시 필수적인 설정. 스프링 시큐리티가 세션을 생성하지 않고, 있어도 사용하지 않겠다.
				.authorizeHttpRequests(a -> a	// 요청에 대한 접근 제어 설정
						.requestMatchers("/h2-console/**").permitAll()
						.requestMatchers("/swagger-ui/index.html", "/goodplassu-server").permitAll()
						.requestMatchers("/token/**").permitAll()
						.requestMatchers("/", "/api/boards/**", "/api/comments/**", "/api/members/**").permitAll()
						.anyRequest().authenticated()
				)
				.oauth2Login(oauth -> oauth		// OAuth2 로그인을 위한 설정. 이 설정을 통해 소셜 로그인이 가능해짐
						.userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))	// 소셜 로그인 성공 후 사용자 정보를 가져오는 서비스를 지정. 필요한 사용자 정보를 데이터베이스에 저장하거나 업데이트함
						.failureHandler(oAuth2LoginFailureHandler)		// 로그인 실패 핸들러
						.successHandler(oAuth2LoginSuccessHandler)		// 로그인 성공 핸들러
				);

		// JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가. 요청 헤더에 포함된 JWT를 검증하여 사용자를 인증하는 역할
		//  UsernamePasswordAuthenticationFilter는 인증되지 않은 사용자의 경우 로그인 페이지로 redirect 시킴
		//  따라서 JWT 토큰을 검증하고, 토큰의 정보를 이용해서 인증 객체를 SecurityContext에 넣어주어야 인증이 되었다고 판단하여 로그인 페이지로 redirect 하지 않음
		http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		http.addFilterBefore(jwtExceptionFilter, JwtAuthFilter.class);

		return http.build();
	}
}
