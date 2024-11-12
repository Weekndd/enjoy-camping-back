package com.ssafy.enjoycamping.trip.sidogugun.service;

import com.ssafy.enjoycamping.trip.sidogugun.dto.GugunDto;
import com.ssafy.enjoycamping.trip.sidogugun.dto.SidoDto;

import java.util.List;

public interface SidogugunService {
	List<SidoDto> getSidos();
	List<GugunDto> getGugunsBySidoCode(int sidoCode);
}
