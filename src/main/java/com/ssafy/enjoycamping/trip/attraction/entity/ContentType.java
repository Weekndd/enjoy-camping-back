package com.ssafy.enjoycamping.trip.attraction.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Builder
@ToString
@Alias("contentType")
public class ContentType {
    private int contentTypeId;
    private String contentTypeName;
}
