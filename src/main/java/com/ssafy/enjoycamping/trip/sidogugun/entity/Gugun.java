package com.ssafy.enjoycamping.trip.sidogugun.entity;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("gugun")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Gugun {
	private int no;
    private int gugunCode;
    private int sidoCode;
    private String gugunName;
}
