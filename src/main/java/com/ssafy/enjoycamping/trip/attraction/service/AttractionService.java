package com.ssafy.enjoycamping.trip.attraction.service;

import com.ssafy.enjoycamping.common.exception.BadRequestException;
import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dao.AttractionDao;
import com.ssafy.enjoycamping.trip.attraction.dao.ContentTypeDao;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDto;
import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import com.ssafy.enjoycamping.trip.attraction.entity.ContentType;
import com.ssafy.enjoycamping.trip.camping.dao.CampingDao;
import com.ssafy.enjoycamping.trip.camping.entity.Camping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttractionService {

	private final AttractionDao attractionDao;
	private final ContentTypeDao contentTypeDao;
	private final CampingDao campingDao;

	public AttractionDto getAttraction(int id) throws BaseException {
		Attraction attraction = attractionDao.selectById(id)
				.orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_ATTRACTION));
		return AttractionDto.fromEntity(attraction);
	}

	public List<AttractionDto> searchAttractions(String keyword, String sidoCode, String gugunCode, String contentType, PagingAndSorting pagingAndSorting) throws BaseException {
		Integer sidoCodeInt = (sidoCode != null && !sidoCode.isEmpty()) ? Integer.parseInt(sidoCode) : null;
		Integer gugunCodeInt = (gugunCode != null && !gugunCode.isEmpty()) ? Integer.parseInt(gugunCode) : null;

		Integer contentTypeId = null;
		if (contentType != null && !contentType.isEmpty()) {
			ContentType type = contentTypeDao.selectByName(contentType)
					.orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_CONTENTTYPE));
			contentTypeId = type.getContentTypeId();
		}

		List<Attraction> attractions = attractionDao.selectByCondition(keyword, sidoCodeInt, gugunCodeInt, contentTypeId, pagingAndSorting);
		return attractions.stream()
				.map(AttractionDto::fromEntity)
				.collect(Collectors.toList());
	}

	public List<AttractionDto> getNearByCampsite(int campingId, PagingAndSorting pagingAndSorting) throws BaseException {
		Camping camping = campingDao.selectById(campingId)
				.orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_CAMPING));

		if ("distance".equals(pagingAndSorting.getOrder())) {
			// 거리 기반으로 가까운 관광지 조회
			return attractionDao.selectAttractionsByDistance(campingId, pagingAndSorting).stream()
					.map(AttractionDto::fromEntity)
					.collect(Collectors.toList());
		} else {
			// 같은 구군에 위치하는 관광지 조회
			return attractionDao.selectAttractionsInSameGugun(campingId, pagingAndSorting).stream()
					.map(AttractionDto::fromEntity)
					.collect(Collectors.toList());
		}
	}
}