package com.ssu.goodplassu.common.config.auth.service;

import com.ssu.goodplassu.common.config.auth.dto.RefreshToken;
import com.ssu.goodplassu.common.config.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void saveTokenInfo(String email, String refreshToken, String accessToken) {
		refreshTokenRepository.save(new RefreshToken(email, accessToken, refreshToken));
	}

	@Transactional
	public void removeRefreshToken(String accessToken) {
		RefreshToken refreshToken = refreshTokenRepository.findByAccessToken(accessToken)
				.orElseThrow(() -> new IllegalArgumentException());

		refreshTokenRepository.delete(refreshToken);
	}
}
