package com.ssu.goodplassu.member.repository;

import com.ssu.goodplassu.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
