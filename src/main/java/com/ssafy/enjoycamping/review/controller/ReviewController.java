package com.ssafy.enjoycamping.review.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;
import com.ssafy.enjoycamping.review.service.ReviewService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reviews")
@Tag(name = "5. REVIEW")
public class ReviewController {
	private ReviewService reviewService;

	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}


	@PostMapping
	public BaseResponse<CreateReviewDto.ResponseCreateReviewDto> createReview(@RequestBody CreateReviewDto.RequestCreateReviewDto request) {
		CreateReviewDto.ResponseCreateReviewDto response = reviewService.createReview(request);
		return new BaseResponse<>(response);
	}
	
	@GetMapping("/{id}")
	public BaseResponse<ReviewDto> getReview(@PathVariable int id) { 
		ReviewDto response = reviewService.getReview(id);
		return new BaseResponse<>(response);
	}
	
	@DeleteMapping("/delete/{id}")
	public BaseResponse<Integer> deleteReview(@PathVariable int id) {
		reviewService.deleteReview(id);
		return new BaseResponse<>(id);
	}
	
	@PatchMapping("/update/{id}")
	public BaseResponse<ReviewDto> updateReview(@RequestBody UpdateReviewDto.RequestUpdateReviewDto request, @PathVariable int id) {
		ReviewDto review = reviewService.updateReview(request, id);
		return new BaseResponse<>(review);
	}
	
	@GetMapping
	public BaseResponse<List<ReviewDto>> getReviews() {
		List<ReviewDto> reviews = reviewService.getReviews();
		return new BaseResponse<>(reviews);
	}
	
	@GetMapping("/camping/{id}")
	public BaseResponse<List<ReviewDto>> findReviewsByCampingId(@PathVariable int campingId) {
		List<ReviewDto> reviews = reviewService.getReviewsByCampingId(campingId);
		return new BaseResponse<>(reviews);
	}
	
	
	
}
