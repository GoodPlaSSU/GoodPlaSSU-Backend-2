package com.ssu.goodplassu.common.config.auth.jwt;

import com.ssu.goodplassu.common.config.auth.dto.GeneratedToken;
import com.ssu.goodplassu.common.config.auth.service.RefreshTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtUtil {
	private final JwtProperties jwtProperties;
	private final RefreshTokenService tokenService;
	private String secretKey;

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
	}

	public GeneratedToken generateToken(String email, String role) {
		// Refresh Token과 Access Token 생성
		String refreshToken = generateRefreshToken(email, role);
		String accessToken = generateAccessToken(email, role);

		// 토큰을 Redis에 저장
		tokenService.saveTokenInfo(email, refreshToken, accessToken);

		return new GeneratedToken(accessToken, refreshToken);
	}

	public String generateRefreshToken(String email, String role) {
		// 토큰의 유효 기간을 밀리 초 단위로 설정
		long refreshPeriod = 1000L * 60L * 60L * 24L * 7;	// 1주

		// 새로운 클레임 객체를 생성하고 email과 role 설정
		Claims claims = Jwts.claims();
		claims.put("email", email);
		claims.put("role", role);

		// 현재 시간과 날짜 불러옴
		Date now = new Date();

		return Jwts.builder()
				.setClaims(claims)	// Payload를 구성하는 속성 정의
				.setIssuedAt(now)	// 발행 일자
				.setExpiration(new Date(now.getTime() + refreshPeriod))	// 만료 일자
				.signWith(SignatureAlgorithm.RS256, secretKey)	// 지정된 서명 알고리즘과 비밀키를 사용해 토큰 서명
				.compact();
	}

	public String generateAccessToken(String email, String role) {
		long accessPeriod = 1000L * 60L * 60L;	// 1시간

		Claims claims = Jwts.claims();
		claims.put("email", email);
		claims.put("role", role);

		Date now = new Date();

		return Jwts.builder()
				.setClaims(claims)	// Payload를 구성하는 속성 정의
				.setIssuedAt(now)	// 발행 일자
				.setExpiration(new Date(now.getTime() + accessPeriod))	// 만료 일자
				.signWith(SignatureAlgorithm.RS256, secretKey)	// 지정된 서명 알고리즘과 비밀키를 사용해 토큰 서명
				.compact();
	}

	public boolean verifyToken(String token) {
		try {
			Jws<Claims> claims = Jwts.parser()
					.setSigningKey(secretKey)	// 비밀키를 설정해 파싱
					.parseClaimsJws(token);		// 주어진 토큰을 파싱해서 Claims 객체 얻음

			// 토큰의 만료 시간과 현재 시간 비교
			return claims.getBody()
					.getExpiration()
					.after(new Date());		// 만료 시간이 현재 시간 이후인지 확인해서 유효성 검사 결과 반환
		} catch (Exception e) {
			return false;
		}
	}

	public String getUid(String token) {
		// 토큰에서 email 추출
		return Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody().getSubject();
	}

	public String getRole(String token) {
		// 토큰에서 role 추출
		return Jwts.parser().setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody().get("role", String.class);
	}
}
