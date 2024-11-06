package com.ssafy.enjoycamping.trip.sidogugun.service;

import com.ssafy.enjoycamping.trip.sidogugun.dao.SidogugunDao;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Gugun;
import com.ssafy.enjoycamping.trip.sidogugun.entity.Sido;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SidogugunServiceImpl implements SidogugunService {
	@Autowired
    private SidogugunDao dao;

    @Override
    public List<Gugun> findBySidoCode(int sidoCode) {
        return dao.findGugunsBySido(sidoCode);
    }

	@Override
	public List<Sido> findAll() {
		return dao.findAllSido();
	}
}