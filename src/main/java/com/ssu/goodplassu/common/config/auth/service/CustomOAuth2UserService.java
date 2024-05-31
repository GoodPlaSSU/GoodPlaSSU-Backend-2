package com.ssu.goodplassu.common.config.auth.service;

import com.ssu.goodplassu.common.config.auth.dto.OAuth2Attributes;
import com.ssu.goodplassu.common.config.auth.dto.SessionUser;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberRepository memberRepository;
	private final HttpSession httpSession;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
		OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
		OAuth2User oAuth2User = delegate.loadUser(oAuth2UserRequest);

		// 로그인 진행 중인 서비스 구분(구글, 네이버, 카카오 등)
		String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

		// OAuth2 로그인 진행 시 키가 되는 필드 값(Primary key와 같은 의미)
		// 구글의 경우 기본적으로 코드를 지원하지만, 네이버와 카카오는 지원하지 않음
		String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
				.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		// OAuth2UserService를 통해 가져온 OAuth2User의 attribute 등을 담을 클래스
		OAuth2Attributes attributes = OAuth2Attributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

		// 사용자 저장 또는 업데이트
		Member member = saveOrUpdate(attributes);

		// 세션에 사용자 정보 저장
		httpSession.setAttribute("user", new SessionUser(member));

		return new DefaultOAuth2User(
				Collections.singleton(new SimpleGrantedAuthority(member.getRoleKey())),
				attributes.getAttributes(),
				attributes.getNameAttributeKey()
		);
	}

	private Member saveOrUpdate(OAuth2Attributes attributes) {
		Member member = memberRepository.findByEmail(attributes.getEmail())
				.map(entity -> entity.update(attributes.getName(), attributes.getPortrait()))
				.orElse(attributes.toEntity());

		return memberRepository.save(member);
	}
}
