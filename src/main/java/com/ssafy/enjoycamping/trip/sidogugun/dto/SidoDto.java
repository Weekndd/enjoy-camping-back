package com.ssafy.enjoycamping.trip.sidogugun.dto;

import com.ssafy.enjoycamping.trip.sidogugun.entity.Sido;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SidoDto {
	private int id;
	private String sidoName;
	private int sidoCode;
	
	public static SidoDto fromEntity(Sido sido) {
		return SidoDto.builder()
				.id(sido.getNo())
				.sidoCode(sido.getSidoCode())
				.sidoName(sido.getSidoName())
				.build();
	}
}
