package com.ssu.goodplassu.board.service;

import com.ssu.goodplassu.board.dto.BoardListResponse;
import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.board.repository.BoardRepository;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.cheer.entity.repository.CheerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
	private final BoardRepository boardRepository;
	private final CheerRepository cheerRepository;

	public List<BoardListResponse> findBoardList(final boolean tag, final String cursor, final Long userId) {
		// 커서 기반 페이지네이션
		Pageable pageable = PageRequest.of(0, 10);
		List<Board> boardList = boardRepository.findBoardListByTagAndCursor(tag, cursor, pageable);

		return boardList.stream().map(board -> {
			// 로그인한 멤버(유저)가 게시물에 좋아요(cheer)를 눌렀는지 확인하기 위함. 로그인하지 않은 상태에서는 null
			Cheer cheer = null;
			if (userId != null)
				cheer = cheerRepository.findByMemberIdAndBoardId(userId, board.getId()).orElse(null);

			return BoardListResponse.of(
					board,
					board.getMember(),
					cheer
			);
		}).collect(Collectors.toList());
	}
}
