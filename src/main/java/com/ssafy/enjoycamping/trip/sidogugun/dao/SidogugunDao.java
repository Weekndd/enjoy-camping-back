package com.ssafy.enjoycamping.trip.sidogugun.dao;

import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Sido;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SidogugunDao {
	public List<Sido> selectSidos();
	public List<Gugun> selectGugunsBySidoId(int sidoCode);
}
