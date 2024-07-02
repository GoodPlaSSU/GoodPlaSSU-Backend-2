package com.ssu.goodplassu.login.service;

import com.ssu.goodplassu.login.entity.RefreshToken;
import com.ssu.goodplassu.login.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
	private final RefreshTokenRepository refreshTokenRepository;

	@Transactional
	public void saveTokenInfo(final String email, final String refreshToken, final String accessToken) {
		Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByEmail(email);
		refreshTokenOptional.ifPresentOrElse(
				resultToken -> resultToken.updateAccessRefreshToken(accessToken, refreshToken),
				() -> refreshTokenRepository.save(new RefreshToken(email, accessToken, refreshToken))
		);
	}

	@Transactional
	public boolean removeRefreshToken(String accessToken) {
		if (accessToken == null) {
			return false;
		} else if (accessToken != null && accessToken.startsWith("Bearer ")) {
			accessToken = accessToken.substring(7);
		}

		Optional<RefreshToken> refreshToken = refreshTokenRepository.findByAccessToken(accessToken);
		return refreshToken.map(
				token -> {
					refreshTokenRepository.delete(token);
					return true;
				}
		).orElse(false);
	}
}
