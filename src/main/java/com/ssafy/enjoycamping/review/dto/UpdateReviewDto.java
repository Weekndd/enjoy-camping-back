package com.ssafy.enjoycamping.review.dto;

import java.util.Set;

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
