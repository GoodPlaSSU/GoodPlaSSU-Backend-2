package com.ssu.goodplassu.login.controller;

import com.ssu.goodplassu.common.dto.ResponseDto;
import com.ssu.goodplassu.login.dto.request.AuthCodeRequest;
import com.ssu.goodplassu.login.dto.response.AuthTokenResponse;
import com.ssu.goodplassu.login.service.AuthService;
import com.ssu.goodplassu.login.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	private final RefreshTokenService refreshTokenService;

	@PostMapping("/google")
	public ResponseEntity<?> authenticateUser(
			@RequestBody AuthCodeRequest authCodeRequest
	) {
		AuthTokenResponse authTokenResponse = authService.getAccessTokenFromGoogle(authCodeRequest.getCode());

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"Access Token을 발급받았습니다.",
						authTokenResponse
				)
		);
	}

	@PostMapping("/refresh")
	public ResponseEntity<?> refreshToken(
			@RequestHeader("Authorization") final String accessToken
	) {
		AuthTokenResponse authTokenResponse = authService.refreshToken(accessToken);
		if (authTokenResponse == null) {
			return new ResponseEntity<>(
					new ResponseDto<>(
							HttpStatus.UNAUTHORIZED.value(),
							"새 Access Token 생성에 실패했습니다.",
							List.of()
					),
					HttpStatus.UNAUTHORIZED
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"새 Access Token을 생성했습니다.",
						authTokenResponse
				)
		);
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logout(
			@RequestHeader("Authorization") final String accessToken
	) {
		boolean isSuccess = refreshTokenService.removeRefreshToken(accessToken);
		if (!isSuccess) {
			return new ResponseEntity<>(
					new ResponseDto<>(
						HttpStatus.BAD_REQUEST.value(),
						"로그아웃에 실패했습니다.",
						List.of()
					),
					HttpStatus.BAD_REQUEST
			);
		}

		return ResponseEntity.ok(
				new ResponseDto<>(
						HttpStatus.OK.value(),
						"로그아웃에 성공했습니다.",
						List.of()
				)
		);
	}
}
