package com.ssu.goodplassu.cheer.entity;

import com.ssu.goodplassu.board.entity.Board;
import com.ssu.goodplassu.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cheer {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private boolean isOn;
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;	// 좋아요를 누른 사람
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Board board;

	public Cheer(
			Member member,
			Board board,
			boolean isOn
	) {
		this.member = member;
		this.board = board;
		this.isOn = isOn;
	}

	public void updateIsOn() {
		this.isOn = this.isOn == true ? false : true;
	}
}
