package com.ssu.goodplassu.cheer.service;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.board.repository.BoardRepository;
import com.ssu.goodplassu.cheer.dto.request.CheerUpdateRequest;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.cheer.repository.CheerRepository;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CheerService {
	private final CheerRepository cheerRepository;
	private final BoardRepository boardRepository;
	private final MemberRepository memberRepository;

	private Pair<Member, Board> isErrorSetCheer(final CheerUpdateRequest cheerUpdateRequest) {
		if (cheerUpdateRequest.getUser_id() == null) {
			return null;	// 로그인하지 않은 상태
		}

		Board board = boardRepository.findById(cheerUpdateRequest.getPost_id()).orElse(null);
		Member member = memberRepository.findById(cheerUpdateRequest.getUser_id()).orElse(null);
		if (board == null || member == null) {
			return null;	// 해당 게시물이 없는 상태 or 해당 멤버가 없는 상태
		}

		return Pair.of(member, board);
	}

	@Transactional
	public Cheer setCheerOn(final CheerUpdateRequest cheerUpdateRequest) {
		Pair<Member, Board> errCheck = isErrorSetCheer(cheerUpdateRequest);
		if (errCheck == null) {
			return null;
		}

		Cheer cheer = cheerRepository.findByMemberIdAndBoardId(cheerUpdateRequest.getUser_id(), cheerUpdateRequest.getPost_id()).orElse(null);
		if (cheer == null) {
			// 로그인 한 유저가 해당 게시물에 아직 좋아요를 누른 적이 없는 상태(최초로 좋아요를 누르는 경우)
			Member writer = memberRepository.findById(errCheck.getRight().getMember().getId()).orElse(null);
			writer.increaseMonthPoint();	// 게시물 작성자의 month point 증가
			writer.increaseTotalPoint();	// 게시물 작성자의 total point 증가
			return cheerRepository.save(new Cheer(errCheck.getLeft(), errCheck.getRight(), true));	// 로그인 한 유저가, 해당 게시물에, 좋아요 눌렀음을 저장
		}

		// 로그인 한 유저가 해당 게시물에 좋아요를 누른 적이 있는 상태(좋아요 눌렀다가 취소한 후 다시 좋아요를 누르는 경우)
		cheer.updateIsOn(true);

		return cheer;
	}

	@Transactional
	public Cheer setCheerOff(final CheerUpdateRequest cheerUpdateRequest) {
		Pair<Member, Board> errCheck = isErrorSetCheer(cheerUpdateRequest);
		if (errCheck == null) {
			return null;
		}

		Cheer cheer = cheerRepository.findByMemberIdAndBoardId(cheerUpdateRequest.getUser_id(), cheerUpdateRequest.getPost_id()).orElse(null);
		if (cheer == null) {
			return null;
		}

		cheer.updateIsOn(false);

		return cheer;
	}
}
