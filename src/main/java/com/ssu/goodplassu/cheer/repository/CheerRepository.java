package com.ssu.goodplassu.cheer.repository;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.cheer.entity.Cheer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CheerRepository extends JpaRepository<Cheer, Long> {
	Optional<Cheer> findByMemberIdAndBoardId(final Long memberId, final Long boardId);
	long countByBoardIdAndIsOnTrue(final Long boardId);

	@Query("SELECT DISTINCT c.board FROM Cheer c WHERE c.member.id = :memberId AND c.isOn = true ORDER BY c.board.createdAt DESC")
	Page<Board> findDistinctBoardsByMemberIdAndIsOnTrueOrderByCreatedAtDesc(@Param("memberId") Long memberId, Pageable pageable);
}
