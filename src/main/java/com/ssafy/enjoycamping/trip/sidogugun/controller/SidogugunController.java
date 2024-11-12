package com.ssafy.enjoycamping.trip.sidogugun.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.trip.sidogugun.dto.GugunDto;
import com.ssafy.enjoycamping.trip.sidogugun.dto.SidoDto;
import com.ssafy.enjoycamping.trip.sidogugun.service.SidogugunService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/sidoguguns")
@AllArgsConstructor
public class SidogugunController {
	private SidogugunService sidogugunService;
	
	@GetMapping("/sidos")
	public BaseResponse<List<SidoDto>> getSidos() {
		List<SidoDto> sidos = sidogugunService.getSidos();
		return new BaseResponse<>(sidos);
	}
	
	@GetMapping("/guguns/{sidoCode}")
	public BaseResponse<List<GugunDto>> getGugunsBySidoCode(@PathVariable int sidoCode) {
		List<GugunDto> guguns = sidogugunService.getGugunsBySidoCode(sidoCode);
		return new BaseResponse<>(guguns);
	}
	
}