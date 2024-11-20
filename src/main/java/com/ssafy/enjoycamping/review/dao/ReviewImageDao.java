package com.ssafy.enjoycamping.review.dao;

import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewImageDao {
	Set<String> selectImageUrlsByReviewId(int id);
	void insert(int reviewId, Set<String> imageUrls);
	void delete(Set<String> imageUrls);
}

