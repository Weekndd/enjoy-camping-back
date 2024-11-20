package com.ssafy.enjoycamping.review.dto;

import java.util.Set;

import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.trip.camping.entity.Camping;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


public class CreateReviewDto {
	@ToString
	@Getter
	@Setter
	@Builder
	public static class RequestCreateReviewDto {
		private int campingId;
		private String title;
		private String content;
		private Set<String> imageUrls;
		
		public Review toEntity(Camping camping, int userId) {
			return Review.builder()
					.writerId(userId)
					.sidoCode(camping.getSidoCode())
					.gugunCode(camping.getGugunCode())
					.campingId(this.campingId)
					.title(this.title)
					.content(this.content)
					.build();
		}
	}
	
	@ToString
	@Getter
	@Setter
	@Builder
	public static class ResponseCreateReviewDto {
		private int id;
	}
	
}
