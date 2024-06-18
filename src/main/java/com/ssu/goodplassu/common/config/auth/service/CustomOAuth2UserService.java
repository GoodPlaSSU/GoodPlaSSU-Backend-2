package com.ssu.goodplassu.common.config.auth.service;

import com.ssu.goodplassu.common.config.auth.dto.OAuth2Attribute;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
	private final MemberRepository memberRepository;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		// 기본 OAuth2UserService 객체 생성
		// DefaultOAuth2UserService는 OAuth2UserService<OAuth2UserRequest, OAuth2User> 인터페이스의 구현체임
		OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

		/**
		 * 사용자가 OAuth 인증에 성공하면,
		 * OAuth2UserRequest 객체에 제공자에 대한 정보, Access Token과 같은 정보를 넣어서
		 * 이 클래스에 넘어옴
		 */

		// OAuth2UserRequest를 전달해서 사용자 정보를 리소스 서버에 요청하고 응답을 받음
		// 응답받은 사용자 정보는 OAuth2UserService를 사용하여 OAuth2User에 매핑됨
		OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

		// 클라이언트 등록 ID(google)와 사용자 이름 속성을 가져옴
		String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
		String userNameAttributeName = oAuth2UserRequest
				.getClientRegistration()
				.getProviderDetails()
				.getUserInfoEndpoint()
				.getUserNameAttributeName();

		// OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체 생성
		// OAuth2Attribute 클래스는 OAuth 인증을 통해 얻어온 사용자의 정보와 속성들(이메일, 닉네임, 프로필 사진 등)을 Map 형태로 반환하기 위해 사용하는 builder 클래스임
		OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

		// OAuth2Attribute의 속성값들을 Map으로 반환받음
		Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

		// 사용자 email 정보를 가져옴
		String email = (String) memberAttribute.get("email");

		// email로 가입된 회원인지 확인
		Optional<Member> findMember = memberRepository.findByEmail(email);

		if (findMember.isEmpty()) {
			// 회원이 존재하지 않을경우, memberAttribute의 exist 값을 false로 넣어준다.
			memberAttribute.put("exist", false);

			// 회원의 권한(기본 권한인 ROLE_USER 설정), 회원 속성, 속성 이름을 이용해 DefaultOAuth2User 객체를 생성해 반환함
			return new DefaultOAuth2User(
					Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
					memberAttribute,
					"email"
			);
		}
		// 회원이 존재할경우, memberAttribute의 exist 값을 true로 넣어준다.
		memberAttribute.put("exist", true);

		// 회원의 권한, 회원 속성, 속성 이름을 이용해 DefaultOAuth2User 객체를 생성해 반환함
		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(findMember.get().getRoleKey())),
				memberAttribute,
				"email"
		);
	}
}
