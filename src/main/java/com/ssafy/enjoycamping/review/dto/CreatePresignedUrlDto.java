package com.ssafy.enjoycamping.review.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class CreatePresignedUrlDto {
    private String fileName;
    private String contentType = "image/jpeg"; // 기본값 설정
}