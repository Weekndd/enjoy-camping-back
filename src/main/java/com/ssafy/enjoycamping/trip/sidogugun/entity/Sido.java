package com.ssafy.enjoycamping.trip.sidogugun.entity;

import org.apache.ibatis.type.Alias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Alias("sido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sido {
	private int no;
    private int sidoCode;
    private String sidoName;

}