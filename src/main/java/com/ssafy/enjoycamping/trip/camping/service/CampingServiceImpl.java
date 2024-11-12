package com.ssafy.enjoycamping.trip.camping.service;

import com.ssafy.enjoycamping.common.exception.BadRequestException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dao.AttractionDao;
import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import com.ssafy.enjoycamping.trip.camping.dao.CampingDao;
import com.ssafy.enjoycamping.trip.camping.dto.CampingDistanceDto;
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
    public List<CampingDto> searchCampings(String keyword, Integer sidoCode, Integer gugunCode, PagingAndSorting pagingAndSorting) {
        List<Camping> campings = campingDao.selectByCondition(keyword, sidoCode, gugunCode, pagingAndSorting);
        return campings.stream()
                .map(CampingDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public int countByCondition(String keyword, Integer sidoCode, Integer gugunCode) {
        return campingDao.countByCondition(keyword, sidoCode, gugunCode);
    }

    @Override
    public List<CampingDistanceDto> getNearByAttraction(int attractionId, PagingAndSorting pagingAndSorting) {
        attractionDao.selectById(attractionId)
                .orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_ATTRACTION));

        // 같은 구군에 위치하는 캠핑장 조회
        return campingDao.selectCampingsInSameGugun(attractionId, pagingAndSorting);
    }

    @Override
    public int countInSameGugun(int attractionId) {
        return campingDao.countInSameGugun(attractionId);
    }
}
