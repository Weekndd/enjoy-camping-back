package com.ssafy.enjoycamping.trip.contenttype.dto;

import com.ssafy.enjoycamping.trip.contenttype.entity.ContentType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
public class ContentTypeDto {
	private Integer id;
	private String name;

	public static ContentTypeDto fromEntity(ContentType contentType) {
		return ContentTypeDto.builder()
				.id(contentType.getContentTypeId())
				.name(contentType.getContentTypeName())
				.build();
	}
}
