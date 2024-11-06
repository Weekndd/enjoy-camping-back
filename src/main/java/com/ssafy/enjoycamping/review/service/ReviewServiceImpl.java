package com.ssafy.enjoycamping.review.service;

import org.springframework.stereotype.Service;

import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
	private final ReviewDao reviewDao;
	
	@Override
	public Review findReviewById(int id) {
		Review review = reviewDao.findReviewById(id);
		return review;
	}


	@Override
	public int createReview(CreateReviewDto.RequestCreateReviewDto createReviewDto) {
		int res = reviewDao.insertReview(createReviewDto);
		return res;
	}

}
