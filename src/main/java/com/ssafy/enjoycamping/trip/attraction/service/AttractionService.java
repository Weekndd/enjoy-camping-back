package com.ssafy.enjoycamping.trip.attraction.service;

import com.ssafy.enjoycamping.common.exception.BadRequestException;
import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dao.AttractionDao;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDistanceDto;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDto;
import com.ssafy.enjoycamping.trip.attraction.entity.Attraction;
import com.ssafy.enjoycamping.trip.camping.dao.CampingDao;
import com.ssafy.enjoycamping.trip.contenttype.dao.ContentTypeDao;
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

	public List<AttractionDto> searchAttractions(String keyword, Integer sidoCode, Integer gugunCode, List<Integer> contentType, PagingAndSorting pagingAndSorting) throws BaseException {
		// 들어온 Content Type 확인
		if (contentType != null && !contentType.isEmpty()) {
			contentType.forEach(c -> {
				contentTypeDao.selectById(c)
						.orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_CONTENTTYPE));
			});
		}

		List<Attraction> attractions = attractionDao.selectByCondition(keyword, sidoCode, gugunCode, contentType, pagingAndSorting);
		return attractions.stream()
				.map(AttractionDto::fromEntity)
				.collect(Collectors.toList());
	}

	public int countByCondition(String keyword, Integer sidoCode, Integer gugunCode, List<Integer> contentType) {
		return attractionDao.countByCondition(keyword, sidoCode, gugunCode, contentType);
	}

	public List<AttractionDistanceDto> getNearByCampsite(int campingId, PagingAndSorting pagingAndSorting) throws BaseException {
		campingDao.selectById(campingId)
				.orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_EXIST_CAMPING));

		// 같은 구군에 위치하는 관광지 조회
		return attractionDao.selectAttractionsInSameGugun(campingId, pagingAndSorting);
	}

	public int countInSameGugun(int attractionId) {
		return attractionDao.countInSameGugun(attractionId);
	}
}