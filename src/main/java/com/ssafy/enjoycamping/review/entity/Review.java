package com.ssafy.enjoycamping.review.entity;

import org.apache.ibatis.type.Alias;

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
	private String createAt;
	private String updatedAt;
}

