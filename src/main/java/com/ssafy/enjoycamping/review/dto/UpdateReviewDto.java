package com.ssafy.enjoycamping.review.dto;

import java.util.List;
import java.util.Set;

import com.ssafy.enjoycamping.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UpdateReviewDto {
	
	@Getter
	@AllArgsConstructor
	@Builder
	public static class RequestUpdateReviewDto {
		private String title;
		private String content;
		private Set<String> imageUrls;
	}
}
