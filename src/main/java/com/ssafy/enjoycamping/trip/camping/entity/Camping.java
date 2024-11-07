package com.ssafy.enjoycamping.trip.camping.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;

@Getter
@Builder
@ToString
@Alias("camping")
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
}
