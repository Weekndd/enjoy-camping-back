package com.ssafy.enjoycamping.trip.camping.controller;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.trip.camping.dto.CampingDto;
import com.ssafy.enjoycamping.trip.camping.service.CampingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/campings")
@Tag(name = "4. CAMPING")
@RequiredArgsConstructor
public class CampingController {

    private final CampingService campingService;

    /**
     * 단건 조회
     */
    @GetMapping("/{index}")
    public BaseResponse<CampingDto> getCamping(@PathVariable("index") int index){
        CampingDto campings = campingService.getCamping(index);
        return new BaseResponse<>(campings);
    }

    /**
     * 필터링 조회 (조건 명시하지 않는 경우 전체 조회)
     */
    @GetMapping
    public BaseResponse<List<CampingDto>> searchCampings(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "sido", required = false) String sidoCode,
            @RequestParam(value = "gugun", required = false) String gugunCode,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageCnt,
            @RequestParam(defaultValue = "name") String order,
            @RequestParam(defaultValue = "asc") String sort){
        PagingAndSorting pagingAndSorting = new PagingAndSorting(pageNo, pageCnt, order, sort);
        List<CampingDto> campings = campingService.searchCampings(keyword, sidoCode, gugunCode, pagingAndSorting);
        return new BaseResponse<>(campings);
    }

    /**
     * 관광지 근처 캠핑장 조회
     */
    @GetMapping("/attractions/{index}")
    public BaseResponse<List<CampingDto>> getNearByCampsite(
            @PathVariable("index") int index,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageCnt,
            @RequestParam(defaultValue = "distance") String order,
            @RequestParam(defaultValue = "asc") String sort){
        PagingAndSorting pagingAndSorting = new PagingAndSorting(pageNo, pageCnt, order, sort);
        List<CampingDto> campings = campingService.getNearByAttraction(index, pagingAndSorting);
        return new BaseResponse<>(campings);
    }
}
