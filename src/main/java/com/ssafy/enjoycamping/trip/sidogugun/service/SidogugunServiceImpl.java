package com.ssafy.enjoycamping.trip.sidogugun.service;

import com.ssafy.enjoycamping.common.exception.NotFoundException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.trip.sidogugun.dao.SidogugunDao;
import com.ssafy.enjoycamping.trip.sidogugun.dto.GugunDto;
import com.ssafy.enjoycamping.trip.sidogugun.dto.SidoDto;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;

import com.ssafy.enjoycamping.trip.sidogugun.entity.Sido;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SidogugunServiceImpl implements SidogugunService {
    private SidogugunDao sidoGugunDao;

	@Override
	public List<SidoDto> getSidos() {
		List<SidoDto> sidos = sidoGugunDao.selectSidos()
				.stream()
				.map(SidoDto::fromEntity)
				.toList();
		return sidos;
	}

	@Override
	public List<GugunDto> getGugunsBySidoCode(int sidoCode) {
		List<Gugun> guguns = sidoGugunDao.selectGugunsBySidoId(sidoCode);
		return guguns.stream()
				.map(GugunDto::fromEntity)
				.toList();
	}

	@Override
	public SidoDto getSido(int index) {
		Sido sido = sidoGugunDao.selectSidoById(index)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_SIDO_GUGUN));
		return SidoDto.fromEntity(sido);
	}

	@Override
	public GugunDto getGugun(int index, int sidoCode) {
		Gugun gugun = sidoGugunDao.selectGugunById(index, sidoCode)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_SIDO_GUGUN));
		return GugunDto.fromEntity(gugun);
	}

}