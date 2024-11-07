package com.ssafy.enjoycamping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto.RequestCreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.review.service.ReviewService;

@SpringBootTest
class ReviewServiceBeanCreateTest {
	@Autowired
//	@Qualifier("reviewServiceImpl")
	private ReviewService reviewService;
	@Autowired
	private ReviewDao reviewDao;
	
	@Test
	void ReviewServiceBeanCreateTest() {
		assertThat(reviewService).isNotNull();
	}
	
	@Test
	@Transactional
	void ReviewCreateTest() {
		CreateReviewDto.RequestCreateReviewDto request = RequestCreateReviewDto.builder()
				.campingId(38)
				.writerId(1)
				.sidoCode(32)
				.gugunCode(13)
				.title("TestTitle3333")
				.content("TestContent3333")
				.build();
		Review review = request.toEntity();
		assertTrue(review.getTitle()== "TestTitle3333", "successfully inserted");
		
		CreateReviewDto.ResponseCreateReviewDto response = reviewService.createReview(request);
		assertTrue(response.getId() > 0, "successfully inserted");
	}
	
	@Test
	void selectByIdTest() {
		ReviewDto reviewDto = reviewService.getReview(11);
		assertTrue(reviewDto.getId()==11,"성공적으로 조회를 완료했습니다.");
	}
}
