package com.ssafy.enjoycamping.review.dto;


import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ReviewDto {
	private int id;
	private int campingId;
	private int writerId;
	private int sidoCode;
	private int gugunCode;
	private String title;
	private String content;
	private String createAt;
	private String updatedAt;
	
	public static ReviewDto fromEntity(Review review) {
		return ReviewDto.builder()
				.id(review.getId())
				.campingId(review.getCampingId())
				.writerId(review.getWriterId())
				.sidoCode(review.getSidoCode())
				.gugunCode(review.getGugunCode())
				.title(review.getTitle())
				.content(review.getContent())
				.createAt(review.getCreateAt())
				.updatedAt(review.getUpdatedAt())
				.build();
	}
}

