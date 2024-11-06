package com.ssafy.enjoycamping.trip.camping.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class Camping {
	private int id;
    private String name;
    private String sidoName;
    private Integer sidoCode;
    private String gugunName;
    private Integer gugunCode;
    private String detailAddress;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String imageUrl;
    private String introduction;
    private String telephone;
    private String homepageUrl;
    
	public Camping(int id, String name, String sidoName, Integer sidoCode, String gugunName, Integer gugunCode,
                   String detailAddress, BigDecimal latitude, BigDecimal longitude, String imageUrl, String introduction,
                   String telephone, String homepageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.sidoName = sidoName;
		this.sidoCode = sidoCode;
		this.gugunName = gugunName;
		this.gugunCode = gugunCode;
		this.detailAddress = detailAddress;
		this.latitude = latitude;
		this.longitude = longitude;
		this.imageUrl = imageUrl;
		this.introduction = introduction;
		this.telephone = telephone;
		this.homepageUrl = homepageUrl;
	}
}
