package com.ssu.goodplassu.member.scheduler;

import com.ssu.goodplassu.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MonthPointScheduler {
	private final MemberService memberService;

	@Scheduled(cron = "0 0 9 1 * ?") // 매달 25일 자정에 실행, 초 분 시 일 월 요일
	public void scheduleResetMonthlyPoints() {
		memberService.resetMonthPointAllMembers();
	}
}
