package com.ssafy.enjoycamping.review.dao;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

@Mapper
public interface ReviewDao {
	int insertReview(CreateReviewDto.RequestCreateReviewDto createReviewDto);
	Review findReviewById(int id);
}

