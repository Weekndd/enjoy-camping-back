package com.ssafy.enjoycamping.review.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.ssafy.enjoycamping.auth.UserPrincipal;
import com.ssafy.enjoycamping.review.dto.CreatePresignedUrlDto;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;
import com.ssafy.enjoycamping.review.service.ReviewService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reviews")
@Tag(name = "5. REVIEW")
@AllArgsConstructor
public class ReviewController {
	private ReviewService reviewService;

	@PostMapping
	public BaseResponse<CreateReviewDto.ResponseCreateReviewDto> createReview(
			@RequestBody CreateReviewDto.RequestCreateReviewDto request) {
		CreateReviewDto.ResponseCreateReviewDto response = reviewService.createReview(request);
		return new BaseResponse<>(response);
	}
	
	@PostMapping("/images/presignedUrl")
	public BaseResponse<URL> createImageUrl(@RequestBody CreatePresignedUrlDto dto) throws IOException {
	    URL imageUrl = reviewService.createImageUrl(dto.getFileName(), dto.getContentType());
	    return new BaseResponse<>(imageUrl);
	}
	
	@GetMapping("/{id}")
	public BaseResponse<ReviewDto> getReview(@PathVariable int id) { 
		ReviewDto response = reviewService.getReview(id);
		return new BaseResponse<>(response);
	}
	
	
	@DeleteMapping("/{index}")
	public BaseResponse<Integer> deleteReview(@PathVariable int index) {
		reviewService.deleteReview(index);
		return new BaseResponse<>(index);
	}
	
	@PatchMapping("/{index}")
	public BaseResponse<ReviewDto> updateReview(@RequestBody UpdateReviewDto.RequestUpdateReviewDto request, @PathVariable int index) {
		ReviewDto review = reviewService.updateReview(request, index);
		return new BaseResponse<>(review);
	}
	
	@GetMapping("/campings/{index}")
	public BaseResponse<List<ReviewDto>> findReviewsByCampingId(@PathVariable int index) {
		List<ReviewDto> reviews = reviewService.getReviewsByCampingId(index);
		return new BaseResponse<>(reviews);
	}
	
	@GetMapping
	public BaseResponse<List<ReviewDto>> getReviews ( //조건으로 검색 및 페이징/정렬 추가
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "sido", required = false) String sido,
			@RequestParam(value = "gugun", required = false) String gugun,
			@RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "5") int pageCnt,
            @RequestParam(defaultValue = "created_at") PagingAndSorting.ReviewOrder order,
            @RequestParam(defaultValue = "desc") PagingAndSorting.Sort sort) {
		PagingAndSorting pagingAndSorting = new PagingAndSorting(pageNo, pageCnt, order, sort);
		List<ReviewDto> reviews = reviewService.getReviewsByCondition(keyword, sido, gugun, pagingAndSorting);
		return new BaseResponse<>(reviews);
	}
	
	@GetMapping("/users")
	public BaseResponse<List<ReviewDto>> getReivewsByUserId(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		List<ReviewDto> reviews = reviewService.getReviewsByUserId(userPrincipal.getUserId());
		return new BaseResponse<>(reviews);
	}
	
	
}
