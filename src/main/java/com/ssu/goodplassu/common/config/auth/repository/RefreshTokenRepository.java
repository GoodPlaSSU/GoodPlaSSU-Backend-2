package com.ssu.goodplassu.common.config.auth.repository;

import com.ssu.goodplassu.common.config.auth.dto.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
	Optional<RefreshToken> findByAccessToken(String accessToken);
}
