package com.ssafy.enjoycamping.review.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoycamping.common.response.BaseResponse;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.service.ReviewService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reviews")
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
	
	
}
