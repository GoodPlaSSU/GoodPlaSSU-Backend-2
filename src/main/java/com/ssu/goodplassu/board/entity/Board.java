package com.ssu.goodplassu.board.entity;

import com.ssu.goodplassu.image.entity.Image;
import com.ssu.goodplassu.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Member member;
	private String content;
	@OneToMany(mappedBy = "board", cascade = {REMOVE, PERSIST}, orphanRemoval = true)
	private List<Image> images = new ArrayList<>();
	private Long viewCount = 0L;
	private Long cheerCount = 0L;
	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	private boolean tag;	// 0: 선행게시판, 1: 참여게시판

	public Board(
			Member member,
			String content,
			boolean tag
	) {
		this.member = member;
		this.content = content;
		this.tag = tag;
	}

	public void increaseViewCount() {
		this.viewCount++;
	}

	public void addImage(Image image) {
		images.add(image);
		image.setBoard(this);
	}

	public void updateData(String content, boolean tag) {
		this.content = content;
		this.tag = tag;
	}
}
