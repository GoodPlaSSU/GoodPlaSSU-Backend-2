package com.ssu.goodplassu.common.config.auth.dto;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@AllArgsConstructor
@RedisHash(value = "jwtToken", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {
	@Id
	private String id;
	@Indexed
	private String accessToken;
	private String refreshToken;

	public void updateAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
