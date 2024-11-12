package com.ssafy.enjoycamping.trip.sidogugun.service;

import com.ssafy.enjoycamping.trip.sidogugun.dao.SidogugunDao;
import com.ssafy.enjoycamping.trip.sidogugun.dto.GugunDto;
import com.ssafy.enjoycamping.trip.sidogugun.dto.SidoDto;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

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
	
}