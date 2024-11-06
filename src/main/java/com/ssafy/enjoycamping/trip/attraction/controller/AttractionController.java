package com.ssafy.enjoycamping.trip.attraction.controller;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDto;
import com.ssafy.enjoycamping.trip.attraction.service.AttractionService;
import com.ssafy.enjoycamping.user.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/attractions")
@Tag(name = "3. ATTRACTION")
@RequiredArgsConstructor
public class AttractionController {

    private final AttractionService attractionService;

    /**
     * 단건 조회
     */
    @GetMapping("/{index}")
    public BaseResponse<AttractionDto> getAttraction(@PathVariable("index") int index){
        AttractionDto attraction = attractionService.getAttraction(index);
        return new BaseResponse<>(attraction);
    }

    /**
     * 필터링 조회 (조건 명시하지 않는 경우 전체 조회)
     */
    @GetMapping
    public BaseResponse<List<AttractionDto>> searchAttractions(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sido", required = false) String sidoCode,
            @RequestParam(value = "gugun", required = false) String gugunCode,
            @RequestParam(value = "contentType", required = false) String contentType,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageCnt,
            @RequestParam(defaultValue = "title") String order,
            @RequestParam(defaultValue = "asc") String sort){
        PagingAndSorting pagingAndSorting = new PagingAndSorting(pageNo, pageCnt, order, sort);
        List<AttractionDto> attractions = attractionService.searchAttractions(keyword, sidoCode, gugunCode, contentType, pagingAndSorting);
        return new BaseResponse<>(attractions);
    }

    /**
     * 캠핑장 근처 관광지 조회
     */
    @GetMapping("/campings/{index}")
    public BaseResponse<List<AttractionDto>> getNearByCampsite(
            @PathVariable("index") int index,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageCnt,
            @RequestParam(defaultValue = "distance") String order,
            @RequestParam(defaultValue = "asc") String sort){
        PagingAndSorting pagingAndSorting = new PagingAndSorting(pageNo, pageCnt, order, sort);
        List<AttractionDto> attractions = attractionService.getNearByCampsite(index, pagingAndSorting);
        return new BaseResponse<>(attractions);
    }
}
