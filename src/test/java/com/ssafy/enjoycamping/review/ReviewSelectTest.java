package com.ssafy.enjoycamping.review;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.service.ReviewService;

@SpringBootTest
class ReviewSelectTest {
	@Autowired
	ReviewService reviewService;
	
	@Test
	void selectByIdTest() {
		ReviewDto reviewDto = reviewService.getReview(11);
		assertTrue(reviewDto.getId()==11,"성공적으로 조회를 완료했습니다.");
	}
	
	@Test
	void selectAllTest() {
		List<ReviewDto> reviews = reviewService.getReviews();
		int expectedSize = 8; //리뷰수에 맞게 설정 할 것
		assertTrue(reviews.size() == expectedSize);
	}	

}
