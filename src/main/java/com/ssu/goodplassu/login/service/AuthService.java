package com.ssu.goodplassu.login.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AuthService {
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
	private final MemberRepository memberRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtUtil jwtUtil;

	@Transactional
	public AuthTokenResponse getAccessTokenFromGoogle(String code) throws IOException, InterruptedException {
		log.debug("======== Authorization Code : " + code);

		GoogleTokenResponse googleTokenResponse = new GoogleAuthorizationCodeTokenRequest(
				new NetHttpTransport(),
				new JacksonFactory(),
				tokenUri,
				clientId,
				clientSecret,
				code,
				redirectUri)
				.execute();
		String accessToken = googleTokenResponse.getAccessToken();
		log.debug("======== Access Token : " + googleTokenResponse.getAccessToken());

		GoogleInfoDto googleInfoDto = getMemberInfoFromGoogle(accessToken);
		UserRegistrationDto userRegistrationDto = googleInfoDto.toUserRegistrationDto();

		GeneratedToken generatedToken = jwtUtil.generateToken(userRegistrationDto.getEmail(), userRegistrationDto.getRole().getKey());

		saveMemberInfoFromGoogle(userRegistrationDto);

		log.debug("======== JWT : " + generatedToken.getAccessToken());

		return AuthTokenResponse.of(generatedToken.getAccessToken());
	}

	private GoogleInfoDto getMemberInfoFromGoogle(String accessToken) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(userUri))
				.header("Authorization", "Bearer " + accessToken)  // 액세스 토큰을 헤더에 추가
				.build();

		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

		ObjectMapper mapper = new ObjectMapper();
		GoogleInfoDto googleInfoDto = mapper.readValue(response.body(), GoogleInfoDto.class);
		log.debug("======== User Info(Email) : " + googleInfoDto.getEmail());

		return googleInfoDto;
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
