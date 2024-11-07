package com.ssafy.enjoycamping.trip.camping.service;

import com.ssafy.enjoycamping.common.exception.BadRequestException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dao.AttractionDao;
import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import com.ssafy.enjoycamping.trip.camping.dao.CampingDao;
import com.ssafy.enjoycamping.trip.camping.dto.CampingDto;
import com.ssafy.enjoycamping.trip.camping.entity.Camping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampingServiceImpl implements CampingService{
    private final CampingDao campingDao;
    private final AttractionDao attractionDao;

    @Override
    public CampingDto getCamping(int index) {
        Camping camping = campingDao.selectById(index)
                .orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_CAMPING));
        return CampingDto.fromEntity(camping);
    }

    @Override
    public List<CampingDto> searchCampings(String keyword, String sidoCode, String gugunCode, PagingAndSorting pagingAndSorting) {
        Integer sidoCodeInt = (sidoCode != null && !sidoCode.isEmpty()) ? Integer.parseInt(sidoCode) : null;
        Integer gugunCodeInt = (gugunCode != null && !gugunCode.isEmpty()) ? Integer.parseInt(gugunCode) : null;

        List<Camping> campings = campingDao.searchCampings(keyword, sidoCodeInt, gugunCodeInt, pagingAndSorting);
        return campings.stream()
                .map(CampingDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CampingDto> getNearByAttraction(int attractionId, PagingAndSorting pagingAndSorting) {
        Attraction attraction = attractionDao.selectById(attractionId)
                .orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_ATTRACTION));

        if ("distance".equals(pagingAndSorting.getOrder())) {
            // 거리 기반으로 가까운 캠핑장 조회
            return campingDao.selectCampingsByDistance(attractionId, pagingAndSorting).stream()
                    .map(CampingDto::fromEntity)
                    .collect(Collectors.toList());
        } else {
            // 같은 구군에 위치하는 캠핑장 조회
            return campingDao.selectCampingsInSameGugun(attractionId, pagingAndSorting).stream()
                    .map(CampingDto::fromEntity)
                    .collect(Collectors.toList());
        }
    }
}
