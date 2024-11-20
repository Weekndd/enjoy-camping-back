package com.ssafy.enjoycamping.review.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.review.entity.ReviewImage;

@Mapper
public interface ReviewDao {
	Optional<Review> selectById(int id);
	List<Review> selectAll();
	
	Set<String> selectAllImageUrl(int id);
	List<Review> selectByCampingId(int campingId);
	int insert(Review review);
	void insertImages(int reviewId, List<String> imageUrls);
	void deleteImages(List<ReviewImage> images);
	int updateImageReviewId(ReviewImage image);
	
	void update(Review review);
	void delete(int id);
	List<Review> selectByCondition(String keyword, Integer sidoCode, Integer gugunCode, PagingAndSorting pagingAndSorting);
	List<Review> selectByUserId(int userId);
	
}

