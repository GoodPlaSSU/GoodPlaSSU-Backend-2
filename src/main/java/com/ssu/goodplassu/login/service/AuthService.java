package com.ssu.goodplassu.login.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.ssu.goodplassu.login.dto.GoogleInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class AuthService {
	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	String clientId;

	public GoogleInfoDto authenticate(String token) {
		return extractUserInfoFromToken(token);
	}

	private GoogleInfoDto extractUserInfoFromToken(String token) {
		try {
			GoogleIdTokenVerifier verifier = createGoogleIdTokenVerifier();
			GoogleIdToken idToken = verifier.verify(token);
			GoogleIdToken.Payload payload = idToken.getPayload();

			return convertPayloadToGoogleInfoDto(payload);
		} catch (GeneralSecurityException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	private GoogleInfoDto convertPayloadToGoogleInfoDto(GoogleIdToken.Payload payload) {
		String email = payload.getEmail();
		String name = payload.get("name").toString();
		String picture = payload.get("picture").toString();
		return GoogleInfoDto.builder()
				.email(email)
				.name(name)
				.picture(picture)
				.build();
	}

	private GoogleIdTokenVerifier createGoogleIdTokenVerifier() {
		return new GoogleIdTokenVerifier.Builder(
				new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(clientId))
				.build();
	}
}
