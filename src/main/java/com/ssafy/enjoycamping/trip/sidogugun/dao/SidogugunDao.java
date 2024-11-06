package com.ssafy.enjoycamping.trip.sidogugun.dao;

import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Sido;

import java.util.List;

public interface SidogugunDao {
	public List<Sido> findAllSido();
	public List<Gugun> findGugunsBySido(int sidoCode);
}
