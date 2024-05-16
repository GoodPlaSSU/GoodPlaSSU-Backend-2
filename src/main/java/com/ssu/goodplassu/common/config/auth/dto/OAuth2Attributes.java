package com.ssu.goodplassu.common.config.auth.dto;

import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.entity.Role;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuth2Attributes {

	private Map<String, Object> attributes;
	private String nameAttributeKey;
	private String name;
	private String email;
	private String portrait;

	@Builder
	public OAuth2Attributes(
			Map<String, Object> attributes,
			String nameAttributeKey,
			String name,
			String email,
			String portrait
	) {
		this.attributes = attributes;
		this.nameAttributeKey = nameAttributeKey;
		this.name = name;
		this.email = email;
		this.portrait = portrait;
	}

	// OAuth2User에서 반환하는 사용자 정보는 Map이므로 값 하나하나를 변환해야 함
	public static OAuth2Attributes of(
			String registrationId,
			String userNameAttributeName,
			Map<String, Object> attributes
	) {
		return ofGoogle(userNameAttributeName, attributes);
	}

	// 구글 생성자
	private static OAuth2Attributes ofGoogle(
			String userNameAttributeName,
			Map<String, Object> attributes
	) {
		return OAuth2Attributes.builder()
				.attributes(attributes)
				.nameAttributeKey(userNameAttributeName)
				.name((String) attributes.get("name"))
				.email((String) attributes.get("email"))
				.portrait((String) attributes.get("picture"))
				.build();
	}

	// Member 엔티티 생성
	public Member toEntity() {
		return Member.builder()
				.name(name)
				.email(email)
				.portrait(portrait)
				.role(Role.USER)
				.build();
	}
}
