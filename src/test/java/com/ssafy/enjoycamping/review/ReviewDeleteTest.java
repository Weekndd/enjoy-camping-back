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
class ReviewDeleteTest {
	@Autowired
	ReviewService reviewService;
	@Autowired
	ReviewDao reviewDao;
	
	@Test
	@Transactional
	void reviewDeleteTest() {
		//Given
		CreateReviewDto.RequestCreateReviewDto request = RequestCreateReviewDto.builder()
				.campingId(38)
				.title("reivew DELETE 기능 테스트")
				.content("reivew DELETE 기능 테스트")
				.build();

		CampingDto camping = CampingDto.builder()
				.id(38)
				.sidoCode(32)
				.gugunCode(13)
				.build();
		
		
		Review review = request.toEntity(camping, 5);
		reviewDao.insert(review);
		
		//When
		reviewService.deleteReview(review.getId());
		
		//Then
		assertFalse(reviewDao.selectById(review.getId())==null,"성공적으로 삭제 완료!");
	}

}
