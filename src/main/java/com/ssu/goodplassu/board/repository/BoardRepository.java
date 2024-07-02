package com.ssu.goodplassu.board.repository;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
	Page<Board> findBoardsByTagOrderByCreatedAtDesc(boolean tag, Pageable pageable);
	Page<Board> findBoardsByMemberOrderByCreatedAtDesc(Member member, Pageable pageable);
}
