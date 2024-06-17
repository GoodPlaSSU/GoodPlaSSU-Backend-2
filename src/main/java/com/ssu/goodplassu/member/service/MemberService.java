package com.ssu.goodplassu.member.service;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.board.repository.BoardRepository;
import com.ssu.goodplassu.cheer.entity.Cheer;
import com.ssu.goodplassu.cheer.repository.CheerRepository;
import com.ssu.goodplassu.comment.repository.CommentRepository;
import com.ssu.goodplassu.common.config.auth.dto.SecurityUserDto;
import com.ssu.goodplassu.member.dto.response.HighestMonthPointResponse;
import com.ssu.goodplassu.member.dto.response.HighestTotalPointResponse;
import com.ssu.goodplassu.member.dto.response.MemberInfoResponse;
import com.ssu.goodplassu.member.dto.response.MemberPostListResponse;
import com.ssu.goodplassu.member.entity.Member;
import com.ssu.goodplassu.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
	private final MemberRepository memberRepository;
	private final BoardRepository boardRepository;
	private final CheerRepository cheerRepository;
	private final CommentRepository commentRepository;

	public List<HighestMonthPointResponse> getHighestMonthPointMembers() {
		List<Member> memberList = memberRepository.findTop4ByOrderByMonthPointDesc();

		List<HighestMonthPointResponse> highestMonthPointResponseList = memberList.stream()
				.map(member -> HighestMonthPointResponse.of(member))
				.collect(Collectors.toList());

		return highestMonthPointResponseList;
	}

	public List<HighestTotalPointResponse> getHighestTotalPointMembers() {
		List<Member> memberList = memberRepository.findTop4ByOrderByTotalPointDesc();

		List<HighestTotalPointResponse> highestTotalPointResponseList = memberList.stream()
				.map(member -> HighestTotalPointResponse.of(member))
				.collect(Collectors.toList());

		return highestTotalPointResponseList;
	}

	public MemberInfoResponse getMemberInfo(final SecurityUserDto userDto) {
		Member member = memberRepository.findByEmail(userDto.getEmail()).orElse(null);
		if (member == null) {
			return null;
		}

		return MemberInfoResponse.of(member);
	}

	public Page<MemberPostListResponse> getMemberPosts(final int page, final SecurityUserDto userDto) {
		Member member = memberRepository.findByEmail(userDto.getEmail()).orElse(null);
		if (member == null) {
			return null;
		}

		Pageable pageable = Pageable.ofSize(10).withPage(page);

		Page<Board> boardList = boardRepository.findBoardsByMemberOrderByCreatedAtDesc(member, pageable);

		List<MemberPostListResponse> filteredBoardList = boardList.stream()
				.map(board -> {
					Cheer cheer = cheerRepository.findByMemberIdAndBoardId(member.getId(), board.getId()).orElse(null);
					long cheerCnt = cheerRepository.countByBoardIdAndIsOnTrue(board.getId());

					long commentCnt = commentRepository.countByBoard(board);

					return MemberPostListResponse.of(
							board,
							cheer,
							cheerCnt,
							commentCnt
					);
				}).collect(Collectors.toList());

		return new PageImpl<>(filteredBoardList, pageable, boardList.getTotalElements());
	}
}
