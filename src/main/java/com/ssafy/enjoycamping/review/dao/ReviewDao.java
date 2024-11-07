package com.ssafy.enjoycamping.review.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

@Mapper
public interface ReviewDao {
	Optional<Review> selectById(int id);
	List<Review> selectAll();
	int insert(Review review);
	void update(Review review);
	void delete(int id);
}

