package com.ssu.goodplassu.login.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.ssu.goodplassu.login.dto.GeneratedToken;
import com.ssu.goodplassu.login.dto.GoogleInfoDto;
import com.ssu.goodplassu.login.dto.UserRegistrationDto;
import com.ssu.goodplassu.login.dto.response.AuthTokenResponse;
import com.ssu.goodplassu.login.entity.RefreshToken;
import com.ssu.goodplassu.login.jwt.util.JwtUtil;
import com.ssu.goodplassu.login.repository.RefreshTokenRepository;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final WebClient webClient;
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String clientSecret;
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String redirectUri;
	@Value("${spring.security.oauth2.client.registration.google.token-uri}")
	private String tokenUri;
	@Value("${spring.security.oauth2.client.registration.google.user-info}")
	private String userUri;
	@Value("${spring.security.oauth2.client.registration.google.authorization-grant-type}")
	private String grantType;
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public AuthTokenResponse getAccessTokenFromGoogle(String code) {
		WebClient.ResponseSpec responseSpec = webClient.post()
				.uri(uriBuilder -> uriBuilder.path(tokenUri)
						.queryParam("client_id", clientId)
						.queryParam("client_secret", clientSecret)
						.queryParam("code", code)
						.queryParam("grant_type", grantType)
						.queryParam("redirect_uri", redirectUri)
						.build()
				).retrieve();

		String accessToken = responseSpec.bodyToMono(GoogleTokenResponse.class)
				.map(response -> response.getAccessToken())
				.block();

		Mono<GoogleInfoDto> googleInfoDtoMono = getMemberInfoFromGoogle(accessToken);
		UserRegistrationDto userRegistrationDto = googleInfoDtoMono.block().toUserRegistrationDto();

		GeneratedToken generatedToken = jwtUtil.generateToken(userRegistrationDto.getEmail(), userRegistrationDto.getRole().getKey());

		saveMemberInfoFromGoogle(userRegistrationDto);

		log.debug("======== JWT : " + generatedToken.getAccessToken());
		log.debug("======== User Info(Email) : " + userRegistrationDto.getEmail());

		return AuthTokenResponse.of(generatedToken.getAccessToken());
	}

	private Mono<GoogleInfoDto> getMemberInfoFromGoogle(String accessToken) {
		return webClient.get()
				.uri(userUri)
				.headers(httpHeaders -> httpHeaders.setBearerAuth(accessToken))
				.retrieve()
				.bodyToMono(GoogleInfoDto.class);
	}

	private void saveMemberInfoFromGoogle(UserRegistrationDto userRegistrationDto) {
		Optional<Member> memberOptional = memberRepository.findByEmail(userRegistrationDto.getEmail());
		memberOptional.ifPresentOrElse(
				member -> member.updateInfo(userRegistrationDto.getName(), userRegistrationDto.getPicture()),
				() -> memberRepository.save(userRegistrationDto.toEntity())
		);
	}

	@Transactional
	public AuthTokenResponse refreshToken(String accessToken) {
		if (accessToken == null) {
			return null;
		} else if (accessToken != null && accessToken.startsWith("Bearer ")) {
			accessToken = accessToken.substring(7);
		}

		// Access Token으로 Refresh Token 객체 조회
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccessToken(accessToken);

		// Refresh Token이 존재하고 유효하다면 실행함
		if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {
			// Refresh Token 객체 꺼내옴
			RefreshToken resultToken = refreshToken.get();

			// 권한과 아이디 추출해서 새로운 Access Token 생성
			String newAccessToken = jwtUtil.generateAccessToken(resultToken.getEmail(), jwtUtil.getRole(resultToken.getRefreshToken()));

			// Access Token 값 수정
			resultToken.updateAccessToken(newAccessToken);

			return AuthTokenResponse.of(newAccessToken);
		} else {
			return null;
		}
	}
}
