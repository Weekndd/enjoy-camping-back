package com.ssafy.enjoycamping.trip.attraction.dao;

import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDistanceDto;
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
	List<Attraction> selectByCondition(String keyword, Integer sidoCode, Integer gugunCode, List<Integer> contentTypeId, PagingAndSorting pagingAndSorting);
	int countByCondition(String keyword, Integer sidoCode, Integer gugunCode, List<Integer> contentType);
	List<AttractionDistanceDto> selectAttractionsInSameGugun(int campingId, PagingAndSorting pagingAndSorting);
	int countInSameGugun(int attractionId);
}
