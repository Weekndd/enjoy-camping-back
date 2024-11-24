package com.ssafy.enjoycamping.review;

import static org.junit.jupiter.api.Assertions.*;

import com.ssafy.enjoycamping.trip.camping.dto.CampingDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto.RequestCreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.review.service.ReviewService;
import com.ssafy.enjoycamping.trip.camping.entity.Camping;

@SpringBootTest
class ReviewCreateTest {
	@Autowired
	ReviewService reviewService;
	
	@Test
	@Transactional
	void ReviewCreateTest() {
		CreateReviewDto.RequestCreateReviewDto request = RequestCreateReviewDto.builder()
				.campingId(38)
				.title("TestTitle3333")
				.content("TestContent3333")
				.build();
		CampingDto camping = CampingDto.builder()
				.id(38)
				.sidoCode(32)
				.gugunCode(13)
				.build();
				
		Review review = request.toEntity(camping, 5);
		assertTrue(review.getTitle()== "TestTitle3333", "successfully inserted");
		
		CreateReviewDto.ResponseCreateReviewDto response = reviewService.createReview(5, request);
		assertTrue(response.getId() > 0, "successfully inserted");
	}

}
