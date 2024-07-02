package com.ssu.goodplassu.member.repository;

import com.ssu.goodplassu.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Optional<Member> findByEmail(String email);
	List<Member> findTop4ByOrderByMonthPointDesc();
	List<Member> findTop4ByOrderByTotalPointDesc();
	@Modifying
	@Query("UPDATE Member m SET m.monthPoint = 0")
	void resetAllMonthPoints();
}
