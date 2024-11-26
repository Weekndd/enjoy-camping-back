package com.ssafy.enjoycamping.trip.attraction.controller;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDistanceDto;
import com.ssafy.enjoycamping.trip.attraction.dto.AttractionDto;
import com.ssafy.enjoycamping.trip.attraction.service.AttractionService;
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
            @RequestParam(value = "sido", required = false) Integer sidoCode,
            @RequestParam(value = "gugun", required = false) Integer gugunCode,
            @RequestParam(value = "contentType", required = false) List<Integer> contentType,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "9") int pageCnt,
            @RequestParam(required = false) PagingAndSorting.AttractionOrder order,
            @RequestParam(required = false) PagingAndSorting.Sort sort){
        PagingAndSorting pagingAndSorting = new PagingAndSorting(pageNo, pageCnt, order, sort);
        List<AttractionDto> attractions = attractionService.searchAttractions(keyword, sidoCode, gugunCode, contentType, pagingAndSorting);
        int totalCount = attractionService.countByCondition(keyword, sidoCode, gugunCode, contentType);

        return new BaseResponse<>(attractions, totalCount);
    }

    /**
     * 캠핑장 근처 관광지 조회
     */
    @GetMapping("/campings/{index}")
    public BaseResponse<List<AttractionDistanceDto>> getNearByCampsite(
            @PathVariable("index") int index,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "9") int pageCnt,
            @RequestParam(defaultValue = "distance") PagingAndSorting.DistanceOrder order,
            @RequestParam(defaultValue = "asc") PagingAndSorting.Sort sort){
        PagingAndSorting pagingAndSorting = new PagingAndSorting(pageNo, pageCnt, order, sort);
        List<AttractionDistanceDto> attractions = attractionService.getNearByCampsite(index, pagingAndSorting);
        int totalCount = attractionService.countInSameGugun(index);

        return new BaseResponse<>(attractions, totalCount);
    }
}
