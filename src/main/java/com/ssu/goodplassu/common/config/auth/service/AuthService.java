package com.ssu.goodplassu.common.config.auth.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Component
@RequiredArgsConstructor
public class AuthService {
	private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String GOOGLE_CLIENT_ID;
	@Value("${spring.security.oauth2.client.registration.google.client-secret}")
	private String GOOGLE_CLIENT_SECRET;
	@Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
	private String GOOGLE_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/google";

	@SneakyThrows
	public String getGoogleAccessToken(String code) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> params = Map.of(
				"code", code,
				"scope", "https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email",
				"client_id", GOOGLE_CLIENT_ID,
				"client_secret", GOOGLE_CLIENT_SECRET,
				"redirect_uri", GOOGLE_REDIRECT_URI,
				"grant_type", "authorization_code"
		);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);

		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			String json = responseEntity.getBody();

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
			String idToken = (String) jsonMap.get("id_token");

			return (String) jsonMap.get("access_token");
		}

		throw new RuntimeException("구글 엑세스 토큰을 가져오는데 실패했습니다.");
	}
}
