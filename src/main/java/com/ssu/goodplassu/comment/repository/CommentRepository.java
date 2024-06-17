package com.ssu.goodplassu.comment.repository;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByBoard(Board board);
	long countByBoard(Board board);
}
