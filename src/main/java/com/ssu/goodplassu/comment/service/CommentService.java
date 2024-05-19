package com.ssu.goodplassu.comment.service;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.comment.dto.response.CommentListResponse;
import com.ssu.goodplassu.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
	private final CommentRepository commentRepository;

	public List<CommentListResponse> findCommentList(final Board board) {
		List<CommentListResponse> commentList = commentRepository.findAllByBoard(board)
				.stream().map(comment -> {
					return CommentListResponse.of(
							comment
					);
				}).collect(Collectors.toList());

		return commentList;
	}
}
