package com.ssafy.enjoycamping.trip.attraction.dao;

import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDto;
import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
@Transactional(readOnly = true)
public interface AttractionDao {
	Optional<Attraction> selectById(int id);
	List<Attraction> selectByCondition(String keyword, Integer sidoCode, Integer gugunCode, Integer contentTypeId, PagingAndSorting pagingAndSorting);
	List<Attraction> selectAttractionsByDistance(int campingId, PagingAndSorting pagingAndSorting);
	List<Attraction> selectAttractionsInSameGugun(int campingId, PagingAndSorting pagingAndSorting);
}
