package com.ssu.goodplassu.cheer.repository;

import com.ssu.goodplassu.cheer.entity.Cheer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheerRepository extends JpaRepository<Cheer, Long> {
	Optional<Cheer> findByMemberIdAndBoardId(final Long memberId, final Long boardId);
	long countByBoardIdAndIsOnTrue(final Long boardId);
}
