package com.ssu.goodplassu.common.config.auth.util;

import com.ssu.goodplassu.common.config.auth.dto.SecurityUserDto;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class SecurityUtils {
	public static SecurityUserDto getUser() {
		return (SecurityUserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
}
