package com.ssafy.enjoycamping.review.entity;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto.RequestUpdateReviewDto;

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
@Alias("review")
public class Review {
	private int id;
	private int campingId;
	private int writerId;
	private int sidoCode;
	private int gugunCode;
	private String title;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public void updateReview(RequestUpdateReviewDto updatedReview) {
		this.title = updatedReview.getTitle();
		this.content = updatedReview.getContent();
	}
}

