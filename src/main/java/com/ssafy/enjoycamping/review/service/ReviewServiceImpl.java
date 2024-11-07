package com.ssafy.enjoycamping.review.service;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.ssafy.enjoycamping.common.exception.BadRequestException;
import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.UnauthorizedException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto.ResponseCreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.user.dao.UserDao;
import com.ssafy.enjoycamping.user.entity.User;
import com.ssafy.enjoycamping.user.util.JwtProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewServiceImpl implements ReviewService {
	private ReviewDao reviewDao;
	private UserDao userDao;

	
	public ReviewServiceImpl(ReviewDao reviewDao, UserDao userDao) {
		this.reviewDao = reviewDao;
		this.userDao = userDao;
	}

	@Override
	public CreateReviewDto.ResponseCreateReviewDto createReview(CreateReviewDto.RequestCreateReviewDto request) {
		int id = JwtProvider.getUserId();
		// JWT로 User 불러오기 //access Token 만료됐는지 확인하기
		User user = userDao.selectActiveById(id)
				.orElseThrow(() -> new UnauthorizedException(BaseResponseStatus.INVALID_USER_JWT));
		
		Review newReview = request.toEntity();
		reviewDao.insert(newReview);
		return CreateReviewDto.ResponseCreateReviewDto.builder()
				.id(newReview.getId())
				.build();
	}
	
	@Override
	public ReviewDto getReview(int id) throws BaseException {
		Review review = reviewDao.selectById(id)
				.orElseThrow(() -> new BadRequestException(BaseResponseStatus.NOT_FOUND));
		return ReviewDto.fromEntity(review);
	}


}
