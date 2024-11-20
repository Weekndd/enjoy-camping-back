package com.ssafy.enjoycamping.review.dto;

import com.ssafy.enjoycamping.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UpdateReviewDto {
	
	@Getter
	@AllArgsConstructor
	public static class RequestUpdateReviewDto {
		private String title;
		private String content;
		
		public void updateReview(Review review) {
			review.setTitle(this.title);
			review.setContent(this.content);
		}
	}
	
}
