package com.ssafy.enjoycamping.trip.camping.service;

import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.camping.dto.CampingDistanceDto;
import com.ssafy.enjoycamping.trip.camping.dto.CampingDto;

import java.util.List;

public interface CampingService {
    CampingDto getCamping(int index);
    List<CampingDto> searchCampings(String keyword, Integer sidoCode, Integer gugunCode, PagingAndSorting pagingAndSorting);
    int countByCondition(String keyword, Integer sidoCode, Integer gugunCode);
    List<CampingDistanceDto> getNearByAttraction(int attractionId, PagingAndSorting pagingAndSorting);
    int countInSameGugun(int attractionId);
}
