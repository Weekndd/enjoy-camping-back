package com.ssafy.enjoycamping.review.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.review.entity.ReviewImage;

@Mapper
public interface ReviewImageDao {
	Set<String> selectImageUrlsByReviewId(int id);
	void insert(int reviewId, Set<String> imageUrls);
	void delete(Set<String> imageUrls);
}

