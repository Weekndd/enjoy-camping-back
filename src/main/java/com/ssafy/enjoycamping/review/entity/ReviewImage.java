package com.ssafy.enjoycamping.review.entity;

import org.apache.ibatis.type.Alias;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Alias("reviewImage")
public class ReviewImage {
	private int id;
	private int reviewId;
	private String imageUrl;
	private int num;
	//num은 삭제하는게,,,?
	//그리고 등록시간 타임스탬프 만들면 좋을듯(새벽 3시에 작성하는 사람도 있으니,,)
}
