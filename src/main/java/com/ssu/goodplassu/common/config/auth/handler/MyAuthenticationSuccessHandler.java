package com.ssu.goodplassu.common.config.auth.handler;

import com.ssu.goodplassu.common.config.auth.dto.GeneratedToken;
import com.ssu.goodplassu.common.config.auth.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtUtil jwtUtil;

	@Value("${client.url}")
	private String redirectUrl;

	@Override
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) throws IOException, ServletException {
		// OAuth2User로 캐스팅해서 인증된 사용자 정보를 가져옴
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

		// 사용자 이메일을 가져옴
		String email = oAuth2User.getAttribute("email");

		// 서비스 제공 플랫폼(Google)이 어디인지 가져옴
		String provider = oAuth2User.getAttribute("provider");

		// OAuth2User로부터 Role을 얻음
		String role = oAuth2User.getAuthorities().stream()
				.findFirst()	// 첫 번째 Role을 찾아옴(Role을 하나만 가지도록 설계함)
				.orElseThrow(() -> new IllegalAccessError())	// 존재하지 않을 시 예외
				.getAuthority();	// Role을 가져옴

		// JWT 발행
		GeneratedToken token = jwtUtil.generateToken(email, role);
		log.info("JWT = {}", token.getAccessToken());

		// Access Token을 쿼리스트링으로 전달하는 URL 생성
		String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
				.queryParam("accessToken", token.getAccessToken())
				.build()
				.encode(StandardCharsets.UTF_8)
				.toUriString();

		log.info("Redirect 준비");

		// 로그인 확인 페이지로 redirect 시킴
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
