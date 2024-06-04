package com.ssu.goodplassu.common.controller;

import com.ssu.goodplassu.common.config.auth.dto.SessionUser;
import com.ssu.goodplassu.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HomeController {
	private MemberRepository memberRepository;

	@GetMapping("/")
	public String home(final HttpSession httpSession) {
		SessionUser user = (SessionUser) httpSession.getAttribute("user");
		return "hello " + user.getEmail();
	}
}
