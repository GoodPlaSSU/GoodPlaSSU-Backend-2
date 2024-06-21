package com.ssu.goodplassu.login.util;

import com.ssu.goodplassu.login.dto.SecurityUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {
	public static SecurityUserDto getUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDto) {
			return (SecurityUserDto) authentication.getPrincipal();
		}
		// 사용자가 인증되지 않았거나, 혹은 인증된 사용자 정보가 SecurityUserDto가 아닌 경우
		return null;
	}
}
