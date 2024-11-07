package com.ssafy.enjoycamping.review.service;

import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

public interface ReviewService {
	CreateReviewDto.ResponseCreateReviewDto createReview(CreateReviewDto.RequestCreateReviewDto request);
	ReviewDto getReview(int id);
	void deleteReview(int id);
	ReviewDto updateReview(UpdateReviewDto.RequestUpdateReviewDto request, int id);
}
