package com.ssafy.enjoycamping.review.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;

public interface ReviewService {
	CreateReviewDto.ResponseCreateReviewDto createReview(CreateReviewDto.RequestCreateReviewDto request);
	String createImageUrl(MultipartFile image) throws IOException;
	ReviewDto getReview(int id);
	void deleteReview(int id);
	ReviewDto updateReview(UpdateReviewDto.RequestUpdateReviewDto request, int id);
	List<ReviewDto> getReviews();
	List<ReviewDto> getReviewsByCampingId(int campingId);
	List<ReviewDto> getReviewsByCondition(String keyword, String sidoCode, String gugunCode, PagingAndSorting pagingAndSorting);
	List<ReviewDto> getReviewsByUserId(int userId);
}
