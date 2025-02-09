package com.ssafy.enjoycamping.review.entity;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("reviewImage")
@Builder
public class ReviewImage {
	private int id;
	private int reviewId;
	private String imageUrl;
}
