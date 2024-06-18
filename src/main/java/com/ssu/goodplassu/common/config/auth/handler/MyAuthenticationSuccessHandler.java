package com.ssu.goodplassu.common.config.auth.handler;

import com.ssu.goodplassu.common.config.auth.dto.GeneratedToken;
import com.ssu.goodplassu.common.config.auth.jwt.JwtUtil;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.entity.Role;
import com.ssu.goodplassu.member.repository.MemberRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;

	@Value("${client.url}")
	private String redirectUrl;

	@Override
	@Transactional
	public void onAuthenticationSuccess(
			HttpServletRequest request,
			HttpServletResponse response,
			Authentication authentication
	) throws IOException, ServletException {
		log.trace("## 로그인 성공 ##");

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
		log.trace("## JWT = ", token.getAccessToken());

		// Access Token을 쿼리스트링으로 전달하는 URL 생성
		String targetUrl = UriComponentsBuilder.fromUriString(redirectUrl)
				.queryParam("accessToken", token.getAccessToken())
				.build()
				.encode(StandardCharsets.UTF_8)
				.toUriString();

		log.trace("## Redirect 준비 ##");

		// CustomOAuth2UserService에서 셋팅한 로그인한 회원 존재 여부를 가져온다.
		boolean isExist = oAuth2User.getAttribute("exist");
		if (isExist == false) {
			memberRepository.save(Member.builder()
					.name(oAuth2User.getAttribute("name"))
					.email(email)
					.portrait(oAuth2User.getAttribute("picture"))
					.role(Role.USER)
					.build()
			);
		}

		// 로그인 확인 페이지로 redirect 시킴
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
}
