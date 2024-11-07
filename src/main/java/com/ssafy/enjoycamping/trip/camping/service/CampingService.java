package com.ssafy.enjoycamping.trip.camping.service;

import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.camping.dto.CampingDto;

import java.util.List;

public interface CampingService {
    CampingDto getCamping(int index);
    List<CampingDto> searchCampings(String keyword, String sidoCode, String gugunCode, PagingAndSorting pagingAndSorting);
    List<CampingDto> getNearByAttraction(int attractionId, PagingAndSorting pagingAndSorting);
}
