package com.ssu.goodplassu.common.config.auth.controller;

import com.ssu.goodplassu.common.config.auth.dto.RefreshToken;
import com.ssu.goodplassu.common.config.auth.jwt.JwtUtil;
import com.ssu.goodplassu.common.config.auth.repository.RefreshTokenRepository;
import com.ssu.goodplassu.common.config.auth.service.RefreshTokenService;
import com.ssu.goodplassu.common.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
	private final RefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenService refreshTokenService;
	private final JwtUtil jwtUtil;
	/*
	private final AuthService authService;

	@GetMapping("/login/oauth2/code/google")
	public ResponseEntity<?> googleCallback(@RequestParam(name = "code") final String code) {
		String googleAccessToken = authService.getGoogleAccessToken(code);
		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						googleAccessToken,
						List.of()
				)
		);
	}
	*/

	@PostMapping("/token/logout")
	public ResponseEntity<?> logout(
			@RequestHeader("Authorization") final String accessToken
	) {
		// Access Token으로 현재 Redis 정보 삭제
		refreshTokenService.removeRefreshToken(accessToken);

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"로그아웃에 성공했습니다.",
						List.of()
				)
		);
	}

	@PostMapping("/token/refresh")
	public ResponseEntity<?> refresh(
			@RequestHeader("Authorization") final String accessToken
	) {
		// Access Token으로 Refresh Token 객체 조회
		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccessToken(accessToken);

		// Refresh Token이 존재하고 유효하다면 실행함
		if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {
			// Refresh Token 객체 꺼내옴
			RefreshToken resultToken = refreshToken.get();

			// 권한과 아이디 추출해서 새로운 Access Token 생성
			String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));

			// Access Token 값 수정
			resultToken.updateAccessToken(newAccessToken);

			refreshTokenRepository.save(resultToken);

			// 새로운 Access Token 반환
			return ResponseEntity.ok(
					new ResponseDto<>(
							HttpStatus.OK.value(),
							"새 Access Token을 생성했습니다.",
							newAccessToken
					)
			);
		}

		return  new ResponseEntity<>(
				new ResponseDto<>(
						HttpStatus.BAD_REQUEST.value(),
						"새 Access Token 생성에 실패했습니다.",
						List.of()
				),
				HttpStatus.BAD_REQUEST
		);
	}
}
