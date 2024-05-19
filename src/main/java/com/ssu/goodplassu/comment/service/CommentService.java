package com.ssu.goodplassu.comment.service;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.board.repository.BoardRepository;
import com.ssu.goodplassu.comment.dto.request.CommentCreateRequest;
import com.ssu.goodplassu.comment.dto.response.CommentCreateResponse;
import com.ssu.goodplassu.comment.dto.response.CommentListResponse;
import com.ssu.goodplassu.comment.entity.Comment;
import com.ssu.goodplassu.comment.repository.CommentRepository;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
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
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

	public List<CommentListResponse> findCommentList(final Board board) {
		List<CommentListResponse> commentList = commentRepository.findAllByBoard(board)
				.stream().map(comment -> {
					return CommentListResponse.of(
							comment
					);
				}).collect(Collectors.toList());

		return commentList;
	}

	@Transactional
	public CommentCreateResponse createComment(final CommentCreateRequest commentCreateRequest) {
		Board board = boardRepository.findById(commentCreateRequest.getBoard_id()).orElse(null);

		Member member = memberRepository.findById(commentCreateRequest.getWriter_id()).orElse(null);

		if (board == null || member == null) {
			return null;
		}

		Comment comment = new Comment(member, board, commentCreateRequest.getContent());
		Comment commentResult = commentRepository.save(comment);

		return CommentCreateResponse.of(commentResult);
	}
}
