package com.ssafy.enjoycamping.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto.RequestCreateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;

import io.jsonwebtoken.lang.Assert;

@SpringBootTest
class ReviewDaoBeanCreateTest {
	@Autowired
	private ReviewDao reviewDao;

	@Test
	void testReviewDaoBeanIsCreated() {
		assertThat(reviewDao).isNotNull();
		
	}
	
	@Test
	@Transactional
	void testInsertReview() {
		Review review = Review.builder()
				.campingId(38)
				.writerId(1)
				.sidoCode(32)
				.gugunCode(13)
				.title("TestTitle111")
				.content("TestContent1111")
				.build();
		int reviewId = reviewDao.insert(review);
		assertTrue(reviewId>0,"insert success!");
		
	}

}
