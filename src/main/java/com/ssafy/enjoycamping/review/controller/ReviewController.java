package com.ssafy.enjoycamping.review.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping
	public ResponseEntity<String> createReview(@RequestBody CreateReviewDto.RequestCreateReviewDto createReviewDto) {
		log.info(createReviewDto.getContent());
		log.info(createReviewDto.getTitle());
		int res = reviewService.createReview(createReviewDto);
		return ResponseEntity.status(HttpStatus.OK).body("create Review successfully");
	}
}
