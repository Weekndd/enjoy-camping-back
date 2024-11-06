package com.ssafy.enjoycamping.trip.attraction.entity;

import lombok.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
@Alias("attraction")
public class Attraction {
	private Integer no;
	private Integer contentId;
	private String title;
	private Integer contentTypeId;
	private Integer sidoCode;
	private Integer gugunCode;
	private String firstImage1;
	private String firstImage2;
	private Integer mapLevel;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private String tel;
	private String addr1;
	private String addr2;
	private String homepage;
	private String overview;
}
