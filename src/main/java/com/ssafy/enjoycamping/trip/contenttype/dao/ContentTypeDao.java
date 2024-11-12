package com.ssafy.enjoycamping.trip.contenttype.dao;

import com.ssafy.enjoycamping.trip.contenttype.entity.ContentType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
@Transactional(readOnly = true)
public interface ContentTypeDao {
	Optional<ContentType> selectById(int id);
	List<ContentType> selectAll();
}
