package com.ssafy.enjoycamping.trip.camping.dao;

import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import com.ssafy.enjoycamping.trip.camping.entity.Camping;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Mapper
@Transactional(readOnly = true)
public interface CampingDao {
    Optional<Camping> selectById(int id);
    List<Camping> searchCampings(String keyword, Integer sidoCode, Integer gugunCode, PagingAndSorting pagingAndSorting);
    List<Camping> selectCampingsByDistance(int attractionId, PagingAndSorting pagingAndSorting);
    List<Camping> selectCampingsInSameGugun(int attractionId, PagingAndSorting pagingAndSorting);
}