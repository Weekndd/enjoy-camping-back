package com.ssafy.enjoycamping.trip.contenttype.controller;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.trip.contenttype.dto.ContentTypeDto;
import com.ssafy.enjoycamping.trip.contenttype.service.ContentTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/contenttypes")
@Tag(name = "6. CONTENTTYPE")
@RequiredArgsConstructor
public class ContentTypeController {

    private final ContentTypeService contentTypeService;

    /**
     * 컨텐츠 타입 조회 (태그)
     */
    @GetMapping
    public BaseResponse<List<ContentTypeDto>> getContentTypes(){
        List<ContentTypeDto> contentTypes = contentTypeService.getContentTypes();
        return new BaseResponse<>(contentTypes);
    }
}
