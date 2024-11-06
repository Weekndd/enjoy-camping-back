package com.ssafy.enjoycamping.review.dto;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@ToString
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class CreateReviewDto {
//	private int campingId;
//	private int writerId;
//	private int sidoCode;
//	private int gugunCode;
//	private String title;
//	private String content;


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
	}
	
	@ToString
	@Getter
	@Setter
	public static class ResponseCreateReviewDto {
		private int id;
	}
}
