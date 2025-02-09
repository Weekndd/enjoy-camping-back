package com.ssafy.enjoycamping.trip.contenttype.service;

import com.ssafy.enjoycamping.common.exception.NotFoundException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.trip.contenttype.dao.ContentTypeDao;
import com.ssafy.enjoycamping.trip.contenttype.dto.ContentTypeDto;
import com.ssafy.enjoycamping.trip.contenttype.entity.ContentType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentTypeService {
	private final ContentTypeDao contentTypeDao;

	public List<ContentTypeDto> getContentTypes(){
		return contentTypeDao.selectAll().stream()
				.map(ContentTypeDto::fromEntity)
				.collect(Collectors.toList());
	}

	public ContentTypeDto getContentType(int index){
		ContentType contentType = contentTypeDao.selectById(index)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_CONTENTTYPE));
		return ContentTypeDto.fromEntity(contentType);
	}
}