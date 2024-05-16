package com.ssu.goodplassu.board.service;

import com.ssu.goodplassu.board.dto.request.PostCreateRequest;
import com.ssu.goodplassu.board.dto.response.BoardDetailResponse;
import com.ssu.goodplassu.board.dto.response.BoardListResponse;
import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.board.repository.BoardRepository;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.cheer.repository.CheerRepository;
import com.ssu.goodplassu.comment.entity.Comment;
import com.ssu.goodplassu.comment.repository.CommentRepository;
import com.ssu.goodplassu.image.entity.Image;
import com.ssu.goodplassu.image.entity.ImageType;
import com.ssu.goodplassu.image.service.ImageService;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
	private final BoardRepository boardRepository;
	private final CheerRepository cheerRepository;
	private final MemberRepository memberRepository;
	private final CommentRepository commentRepository;
	private final ImageService imageService;

	public Page<BoardListResponse> findBoardList(final boolean tag, final Long userId, final int page) {
		Pageable pageable = Pageable.ofSize(10).withPage(page);
		Page<Board> boardList = boardRepository.findBoardsByTagOrderByCreatedAtDesc(tag, pageable);

		List<BoardListResponse> filteredBoardList = boardList.stream()
				.map(board -> {
					// 로그인한 멤버(유저)가 게시물에 좋아요(cheer)를 눌렀는지 확인하기 위함. 로그인하지 않은 상태에서는 null
					Cheer cheer = null;
					if (userId != null)
						cheer = cheerRepository.findByMemberIdAndBoardId(userId, board.getId()).orElse(null);

					List<Comment> commentList = commentRepository.findAllByBoard(board);

					return BoardListResponse.of(
							board,
							board.getMember(),
							cheer,
							commentList.size()
					);
				}).collect(Collectors.toList());

		return new PageImpl<>(filteredBoardList, pageable, boardList.getTotalElements());
	}

	@Transactional
	public BoardDetailResponse findBoardById(final Long postId) {
		Board board = boardRepository.findById(postId).orElse(null);
		if (board == null) {
			return null;
		}

		board.increaseViewCount();

		Member member = board.getMember();
		member.increaseTotalPoint();
		member.increaseMonthPoint();

		return BoardDetailResponse.of(board, member);
	}

	@Transactional
	public Long createPost(
			final PostCreateRequest postCreateRequest,
			final List<MultipartFile> multipartFiles
	) {
		Member member = memberRepository.findById(postCreateRequest.getWriter_id()).orElse(null);
		if (member == null) {
			return null;
		}

		Board board = new Board(
				member,
				postCreateRequest.getContent(),
				postCreateRequest.isTag()
		);

		if (multipartFiles != null) {
			List<Image> images = imageService.uploadImages(multipartFiles, ImageType.POST.name());
			images.forEach(board::addImage);
		}

		boardRepository.save(board);

		member.increaseMonthPoint();
		member.increaseTotalPoint();

		return member.getId();
	}
}
