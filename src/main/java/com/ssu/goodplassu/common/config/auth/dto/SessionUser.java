package com.ssu.goodplassu.common.config.auth.dto;

import com.ssu.goodplassu.member.entity.Member;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

	private String name;
	private String email;
	private String portrait;
	public SessionUser(Member member) {
		this.name = member.getName();
		this.email = member.getEmail();
		this.portrait = member.getPortrait();
	}
}
