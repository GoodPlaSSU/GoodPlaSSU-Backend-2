package com.ssu.goodplassu.common.config.auth.jwt.filter;

import com.ssu.goodplassu.common.config.auth.dto.SecurityUserDto;
import com.ssu.goodplassu.common.config.auth.jwt.JwtUtil;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getRequestURI().contains("/token/refresh");
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// request Header에서 Access Token 가져옴
		String accessToken = request.getHeader("Authorization");

		// 토큰 검사 생략(모두 허용 URL의 경우 토큰 검사 통과)
		if (!StringUtils.hasText(accessToken)) {
			doFilter(request, response, filterChain);
			return;
		}

		// Access Token 검증, 만료되었을 경우 예외 발생
		if (!jwtUtil.verifyToken(accessToken)) {
			throw new RuntimeException("Access Token 만료됨");
		}

		// Access Token의 값이 있고, 유효한 경우
		if (jwtUtil.verifyToken(accessToken)) {
			// Access Token 내부의 Payload에 있는 email로 사용자 조회. 없으면 예외 발생
			Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
					.orElseThrow(() -> new IllegalStateException());

			// Security Context에 등록할 User 객체 생성
			SecurityUserDto securityUserDto = SecurityUserDto.builder()
					.id(findMember.getId())
					.email(findMember.getEmail())
					.role(findMember.getRoleKey())
					.nickname(findMember.getName())
					.build();

			// Security Context에 인증 객체 등록
			Authentication authentication = getAuthentication(securityUserDto);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	public Authentication getAuthentication(SecurityUserDto securityUserDto) {
		return new UsernamePasswordAuthenticationToken(
				securityUserDto, "",
				List.of(new SimpleGrantedAuthority(securityUserDto.getRole()))
		);
	}
}
