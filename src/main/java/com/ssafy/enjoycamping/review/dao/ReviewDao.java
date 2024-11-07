package com.ssafy.enjoycamping.review.dao;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

@Mapper
public interface ReviewDao {
	int insert(Review review);
	Optional<Review> selectById(int id);
	void delete(int id);
	void update(Review review);
}

