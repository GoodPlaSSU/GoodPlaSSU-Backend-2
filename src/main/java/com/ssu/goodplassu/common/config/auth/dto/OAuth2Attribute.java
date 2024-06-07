package com.ssu.goodplassu.common.config.auth.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class OAuth2Attribute {
	private Map<String, Object> attributes;	// 사용자 속성 정보를 담는 Map
	private String attributeKey;			// 사용자 속성의 키 값
	private String email;
	private String name;
	private String picture;
	private String provider;

	// 서비스에 따라 OAuth2Attribute 객체를 생성하는 메서드
	public static OAuth2Attribute of(
			String provider,
			String attributeKey,
			Map<String, Object> attributes
	) {
		if (provider.equals("google")) {
			return ofGoogle(provider, attributeKey, attributes);
		}
		else {
			throw new RuntimeException();
		}
	}

	// Google 로그인에 사용하는 메서드
	// 사용자 정보가 따로 Wrapping 되지 않고 제공되어 바로 get() 메서드로 접근이 가능함
	private static OAuth2Attribute ofGoogle(
			String provider,
			String attributeKey,
			Map<String, Object> attributes
	) {
		return OAuth2Attribute.builder()
				.email((String) attributes.get("email"))
				.provider(provider)
				.attributes(attributes)
				.attributeKey(attributeKey)
				.build();
	}

	// OAuth2User 객체에 넣어주기 위해 Map으로 값을 반환함
	public Map<String, Object> convertToMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", attributeKey);
		map.put("key", attributeKey);
		map.put("email", email);
		map.put("provider", provider);

		return map;
	}
}
