package com.ssafy.enjoycamping.review;

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
	private ReviewService reviewService;
	@Test
	void ReviewServiceBeanCreateTest() {
		assertThat(reviewService).isNotNull();
	}
}
