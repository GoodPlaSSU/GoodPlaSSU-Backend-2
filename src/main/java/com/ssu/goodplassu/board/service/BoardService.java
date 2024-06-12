package com.ssu.goodplassu.board.service;

import com.ssu.goodplassu.board.dto.request.PostCreateRequest;
import com.ssu.goodplassu.board.dto.request.PostModifyRequest;
import com.ssu.goodplassu.board.dto.response.BoardDetailResponse;
import com.ssu.goodplassu.board.dto.response.BoardListResponse;
import com.ssu.goodplassu.board.dto.response.PostCreateResponse;
import com.ssu.goodplassu.board.dto.response.PostModifyResponse;
import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.board.repository.BoardRepository;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.cheer.repository.CheerRepository;
import com.ssu.goodplassu.comment.dto.response.CommentListResponse;
import com.ssu.goodplassu.comment.entity.Comment;
import com.ssu.goodplassu.comment.repository.CommentRepository;
import com.ssu.goodplassu.comment.service.CommentService;
import com.ssu.goodplassu.common.config.auth.dto.SecurityUserDto;
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

import java.util.ArrayList;
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
	private final CommentService commentService;

	public Page<BoardListResponse> findBoardList(
			final boolean tag,
			final int page,
			final SecurityUserDto userDto
	) {
		Pageable pageable = Pageable.ofSize(10).withPage(page);
		Page<Board> boardList = boardRepository.findBoardsByTagOrderByCreatedAtDesc(tag, pageable);

		List<BoardListResponse> filteredBoardList = boardList.stream()
				.map(board -> {
					// 로그인한 멤버(유저)가 게시물에 좋아요(cheer)를 눌렀는지 확인하기 위함. 로그인하지 않은 상태에서는 null
					Cheer cheer = null;
					List<Cheer> cheerOnList = new ArrayList<>();

					if (userDto != null) {
						cheer = cheerRepository.findByMemberIdAndBoardId(userDto.getId(), board.getId()).orElse(null);
						cheerOnList = cheerRepository.findByBoardIdAndIsOnTrue(board.getId());
					}

					List<Comment> commentList = commentRepository.findAllByBoard(board);

					return BoardListResponse.of(
							board,
							board.getMember(),
							cheer,
							cheerOnList.size(),
							commentList.size()
					);
				}).collect(Collectors.toList());

		return new PageImpl<>(filteredBoardList, pageable, boardList.getTotalElements());
	}

	@Transactional
	public BoardDetailResponse findBoardById(final Long postId, final SecurityUserDto userDto) {
		Board board = boardRepository.findById(postId).orElse(null);
		if (board == null) {
			return null;
		}

		board.increaseViewCount();

		Member member = board.getMember();
		member.increaseTotalPoint();
		member.increaseMonthPoint();

		List<CommentListResponse> commentListResponseList = commentService.findCommentList(board);
		List<Cheer> cheerOnList = cheerRepository.findByBoardIdAndIsOnTrue(board.getId());

		boolean isOn = true;
		Cheer cheer = null;
		if (userDto != null) {
			cheer = cheerRepository.findByMemberIdAndBoardId(userDto.getId(), board.getId()).orElse(null);
		}
		if (cheer == null || cheer.isOn() == false) {
			isOn = false;
		}

		return BoardDetailResponse.of(board, member, commentListResponseList, cheerOnList.size(), isOn);
	}

	@Transactional
	public PostCreateResponse createPost(
			final PostCreateRequest postCreateRequest,
			final List<MultipartFile> multipartFiles,
			final SecurityUserDto userDto
	) {
		if (userDto == null) {
			return null;
		}

		Member member = memberRepository.findByEmail(userDto.getEmail()).orElse(null);
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

		Board boardResult = boardRepository.save(board);

		member.increaseMonthPoint();
		member.increaseTotalPoint();

		return PostCreateResponse.of(boardResult.getId());
	}

	@Transactional
	public PostModifyResponse modifyPost(
			final Long postId,
			final PostModifyRequest postModifyRequest,
			final List<MultipartFile> multipartFiles,
			final SecurityUserDto userDto
	) {
		if (userDto == null) {
			return null;
		}

		Board board = boardRepository.findById(postId).orElse(null);
		if (board == null) {
			return null;
		}

		if (board.getMember().getEmail() != userDto.getEmail()) {
			return null;
		}

		if (multipartFiles != null) {
			List<Image> images = imageService.uploadImages(multipartFiles, ImageType.POST.name());
			images.forEach(board::addImage);
		}

		if (postModifyRequest.getDelete_images() != null) {
			postModifyRequest.getDelete_images().stream()
					.map(imageUrl -> imageService.getImageIdAndDeleteImage(imageUrl))
					.forEach(imageId -> imageService.deleteImageInS3Bucket(imageId));
		}

		board.updateData(postModifyRequest.getContent(), postModifyRequest.isTag());

		return PostModifyResponse.of(board);
	}

	@Transactional
	public Board deletePost(final Long postId, final SecurityUserDto userDto) {
		if (userDto == null) {
			return null;
		}

		Board board = boardRepository.findById(postId).orElse(null);
		if (board == null) {
			return null;
		}

		if (board.getMember().getEmail() != userDto.getEmail()) {
			return null;
		}

		if (board.getImages() != null) {
			board.getImages().stream()
					.map(image -> imageService.getImageIdAndDeleteImage(image.getUrl()))
					.forEach(imageId -> imageService.deleteImageInS3Bucket(imageId));
		}

		boardRepository.delete(board);

		return board;
	}
}
