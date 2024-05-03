package com.ssu.goodplassu.board.repository;

import com.ssu.goodplassu.board.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
	@Query("SELECT b FROM Board b WHERE b.tag = :tag AND CONCAT(FUNCTION('TO_CHAR', b.createdAt, 'YYYYMMDDHH24MISS'), LPAD(CAST(b.id AS string), 10, '0')) < :cursor ORDER BY b.createdAt DESC, b.id DESC")
	List<Board> findBoardListByTagAndCursor(@Param("tag") boolean tag, @Param("cursor") String cursor, Pageable pageable);
}
