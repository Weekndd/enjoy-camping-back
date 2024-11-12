package com.ssafy.enjoycamping.review.dao;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.entity.Review;

@Mapper
public interface ReviewDao {
	Optional<Review> selectById(int id);
	List<Review> selectAll();
	List<Review> selectByCampingId(int campingId);
	int insert(Review review);
	void update(Review review);
	void delete(int id);
	List<Review> selectByCondition(String keyword, Integer sidoCode, Integer gugunCode, PagingAndSorting pagingAndSorting);
	List<Review> selectByUserId(int userId);
	
}

