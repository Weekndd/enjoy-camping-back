package com.ssafy.enjoycamping.trip.sidogugun.dao;

import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Sido;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SidogugunDao {
	List<Sido> selectSidos();
	List<Gugun> selectGugunsBySidoId(int sidoCode);
	Optional<Sido> selectSidoById(int index);
	Optional<Gugun> selectGugunById(int index, int sidoCode);
}
