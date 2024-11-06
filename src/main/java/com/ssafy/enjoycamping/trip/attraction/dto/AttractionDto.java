package com.ssafy.enjoycamping.trip.attraction.dto;

import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@ToString
public class AttractionDto {
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

	public static AttractionDto fromEntity(Attraction attraction) {
		return AttractionDto.builder()
				.no(attraction.getNo())
				.contentId(attraction.getContentId())
				.title(attraction.getTitle())
				.contentTypeId(attraction.getContentTypeId())
				.sidoCode(attraction.getSidoCode())
				.gugunCode(attraction.getGugunCode())
				.firstImage1(attraction.getFirstImage1())
				.firstImage2(attraction.getFirstImage2())
				.mapLevel(attraction.getMapLevel())
				.latitude(attraction.getLatitude())
				.longitude(attraction.getLongitude())
				.tel(attraction.getTel())
				.addr1(attraction.getAddr1())
				.addr2(attraction.getAddr2())
				.homepage(attraction.getHomepage())
				.overview(attraction.getOverview())
				.build();
	}
}
