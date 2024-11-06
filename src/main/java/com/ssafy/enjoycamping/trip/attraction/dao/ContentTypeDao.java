package com.ssafy.enjoycamping.trip.attraction.dao;

import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import com.ssafy.enjoycamping.trip.attraction.entity.ContentType;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
@Transactional(readOnly = true)
public interface ContentTypeDao {
	Optional<ContentType> selectByName(String name);
}
