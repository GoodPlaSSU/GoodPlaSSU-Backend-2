package com.ssu.goodplassu.image.entity;

import com.ssu.goodplassu.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Image {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 1000)
	private String imageId; // ncp 사용시 keyName이 들어감
	private String name;
	@Column(length = 1000)
	private String url;
	private Long size;
	@ManyToOne(fetch = FetchType.LAZY)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Board board;

	public Image(final String imageId, final String name, final String url, final Long size) {
		this.imageId = imageId;
		this.name = name;
		this.url = url;
		this.size = size;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
