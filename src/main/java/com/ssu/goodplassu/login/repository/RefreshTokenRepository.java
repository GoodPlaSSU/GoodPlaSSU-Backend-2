package com.ssu.goodplassu.login.repository;

import com.ssu.goodplassu.login.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
	Optional<RefreshToken> findByAccessToken(String accessToken);
	Optional<RefreshToken> findByEmail(String email);
}
