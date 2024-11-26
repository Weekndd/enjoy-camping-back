package com.ssafy.enjoycamping.trip.sidogugun.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="7. SIDOGUGUN")
@AllArgsConstructor
public class SidogugunController {
	private SidogugunService sidogugunService;
	
	@GetMapping("/sidos")
	public BaseResponse<List<SidoDto>> getSidos() {
		List<SidoDto> sidos = sidogugunService.getSidos();
		return new BaseResponse<>(sidos);
	}

	@GetMapping("/sidos/{index}")
	public BaseResponse<SidoDto> getSido(@PathVariable int index) {
		SidoDto sido = sidogugunService.getSido(index);
		return new BaseResponse<>(sido);
	}
	
	@GetMapping("/sidos/{sidoCode}/guguns")
	public BaseResponse<List<GugunDto>> getGugunsBySidoCode(@PathVariable int sidoCode) {
		List<GugunDto> guguns = sidogugunService.getGugunsBySidoCode(sidoCode);
		return new BaseResponse<>(guguns);
	}

	@GetMapping("/sidos/{sidoCode}/guguns/{index}")
	public BaseResponse<GugunDto> getGugun(@PathVariable int sidoCode, @PathVariable int index) {
		GugunDto gugun = sidogugunService.getGugun(index, sidoCode);
		return new BaseResponse<>(gugun);
	}
	
}