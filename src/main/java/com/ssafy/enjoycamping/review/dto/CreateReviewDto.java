package com.ssafy.enjoycamping.review.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.review.entity.ReviewImage;

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
		private int writerId;
		private int sidoCode;
		private int gugunCode;
		private String title;
		private String content;
		private List<String> imageUrls;
		
		public Review toEntity() {
			return Review.builder()
					.campingId(this.campingId)
					.writerId(this.writerId)
					.sidoCode(this.sidoCode)
					.gugunCode(this.gugunCode)
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
