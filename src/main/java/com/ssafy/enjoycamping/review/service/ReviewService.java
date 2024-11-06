package com.ssafy.enjoycamping.review.service;

import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

public interface ReviewService {
	int createReview(CreateReviewDto.RequestCreateReviewDto createReviewDto);
	Review findReviewById(int id);
}
