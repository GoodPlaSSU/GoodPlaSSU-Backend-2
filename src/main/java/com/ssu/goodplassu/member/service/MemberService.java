package com.ssu.goodplassu.member.service;

import com.ssu.goodplassu.common.config.auth.dto.SecurityUserDto;
import com.ssu.goodplassu.member.dto.response.HighestMonthPointResponse;
import com.ssu.goodplassu.member.dto.response.HighestTotalPointResponse;
import com.ssu.goodplassu.member.dto.response.MemberInfoResponse;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
	private final MemberRepository memberRepository;

	public List<HighestMonthPointResponse> getHighestMonthPointMembers() {
		List<Member> memberList = memberRepository.findTop4ByOrderByMonthPointDesc();

		List<HighestMonthPointResponse> highestMonthPointResponseList = memberList.stream()
				.map(member -> HighestMonthPointResponse.of(member))
				.collect(Collectors.toList());

		return highestMonthPointResponseList;
	}

	public List<HighestTotalPointResponse> getHighestTotalPointMembers() {
		List<Member> memberList = memberRepository.findTop4ByOrderByTotalPointDesc();

		List<HighestTotalPointResponse> highestTotalPointResponseList = memberList.stream()
				.map(member -> HighestTotalPointResponse.of(member))
				.collect(Collectors.toList());

		return highestTotalPointResponseList;
	}

	public MemberInfoResponse getMemberInfo(SecurityUserDto userDto) {
		Member member = memberRepository.findByEmail(userDto.getEmail()).orElse(null);
		if (member == null) {
			return null;
		}

		return MemberInfoResponse.of(member);
	}
}
