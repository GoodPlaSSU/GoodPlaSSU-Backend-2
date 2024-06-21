package com.ssu.goodplassu.comment.repository;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByBoard(Board board);
	long countByBoard(Board board);
	@Query("SELECT DISTINCT c.board FROM Comment c WHERE c.member.id = :memberId ORDER BY c.board.createdAt DESC")
	Page<Board> findDistinctBoardsByMemberIdOrderByCreatedAtDesc(@Param("memberId") Long memberId, Pageable pageable);
}
