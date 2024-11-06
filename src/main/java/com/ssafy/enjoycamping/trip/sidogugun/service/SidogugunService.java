package com.ssafy.enjoycamping.trip.sidogugun.service;

import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Sido;

import java.util.List;

public interface SidogugunService {
	List<Sido> findAll();
	List<Gugun> findBySidoCode(int sidoCode);
}
