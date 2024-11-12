package com.ssafy.enjoycamping.trip.sidogugun.dto;

import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GugunDto {
	private int id;
	private int sidoCode;
	private int gugunCode;
	private String gugunName;
	
	public static GugunDto fromEntity(Gugun gugun) {
		return GugunDto.builder()
				.id(gugun.getNo())
				.sidoCode(gugun.getSidoCode())
				.gugunCode(gugun.getGugunCode())
				.gugunName(gugun.getGugunName())
				.build();
	}
}
