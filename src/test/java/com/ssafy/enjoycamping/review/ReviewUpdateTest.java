package com.ssafy.enjoycamping.review;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.enjoycamping.common.exception.NotFoundException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto.RequestCreateReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto.RequestUpdateReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.review.service.ReviewService;

@SpringBootTest
class ReviewUpdateTest {
	@Autowired
	ReviewService reviewService;
	@Autowired
	ReviewDao reviewDao;
	
	@Test
	@Transactional
	void reviewUpdateTest() {
		//GIVEN 
		CreateReviewDto.RequestCreateReviewDto createRequest = RequestCreateReviewDto.builder()
				.campingId(38)
				.writerId(1)
				.sidoCode(32)
				.gugunCode(13)
				.title("reivew 수정 기능 테스트")
				.content("reivew 수정 기능 테스트")
				.build();
		Review originReview = createRequest.toEntity();
		reviewDao.insert(originReview);
		
		//WHEN
		UpdateReviewDto.RequestUpdateReviewDto updateRequest = RequestUpdateReviewDto.builder()
				.title("new Title")
				.content("new Content")
				.build();
		reviewService.updateReview(updateRequest, originReview.getId());
		
		//THEN
		Review updatedReview = reviewDao.selectById(originReview.getId())
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_REVIEW));
		
		assertFalse(updatedReview.getTitle() == originReview.getTitle());
		
	}
}
