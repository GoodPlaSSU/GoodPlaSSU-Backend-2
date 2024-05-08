package com.ssu.goodplassu.common.config;

import com.ssu.goodplassu.common.config.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
		http
				.cors(c -> c.configure(http))
				.csrf(c -> c.disable())
				.authorizeHttpRequests(a -> a
						.requestMatchers(
								new MvcRequestMatcher(introspector, "/h2-console/**"),
								new AntPathRequestMatcher("/**")
						).permitAll()
						.anyRequest().authenticated()
				)
				.logout(
						logoutConfig -> logoutConfig.logoutSuccessUrl("/")
				)
				.oauth2Login(
						oauth -> oauth.userInfoEndpoint(
								endpoint -> endpoint.userService(customOAuth2UserService)
						)
				);

		return http.build();
	}
}
