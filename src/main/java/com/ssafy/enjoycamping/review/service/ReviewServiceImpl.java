package com.ssafy.enjoycamping.review.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.ssafy.enjoycamping.common.exception.BadRequestException;
import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.NotFoundException;
import com.ssafy.enjoycamping.common.exception.UnauthorizedException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto.ResponseCreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.trip.camping.dao.CampingDao;
import com.ssafy.enjoycamping.trip.camping.entity.Camping;
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
	private CampingDao campingDao;
	
	public ReviewServiceImpl(ReviewDao reviewDao, UserDao userDao, CampingDao campingDao) {
		this.reviewDao = reviewDao;
		this.userDao = userDao;
		this.campingDao = campingDao;
	}

	@Override
	public CreateReviewDto.ResponseCreateReviewDto createReview(CreateReviewDto.RequestCreateReviewDto request) {
//		int id = JwtProvider.getUserId();
//		// JWT로 User 불러오기 //access Token 만료됐는지 확인하기
//		User user = userDao.selectActiveById(id)
//				.orElseThrow(() -> new UnauthorizedException(BaseResponseStatus.INVALID_USER_JWT));
		
		Review newReview = request.toEntity();
		reviewDao.insert(newReview);
		return CreateReviewDto.ResponseCreateReviewDto.builder()
				.id(newReview.getId())
				.build();
	}
	
	@Override
	public ReviewDto getReview(int id) throws BaseException {
		Review review = reviewDao.selectById(id)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_REVIEW));
		return ReviewDto.fromEntity(review);
	}

	@Override
	public void deleteReview(int id) {
		//TODO: 로그인 유저와 작성자 확인 후 맞으면 삭제하는 로직
		Review review = reviewDao.selectById(id)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_REVIEW));
		reviewDao.delete(id);
	}

	@Override
	public ReviewDto updateReview(UpdateReviewDto.RequestUpdateReviewDto request, int id) {
		//TODO: 로그인 유저와 작성자 확인 후 맞으면 업데이트하는 로직
		Review review = reviewDao.selectById(id)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_REVIEW));
		
		request.updateReview(review);
		reviewDao.update(review);
		return ReviewDto.fromEntity(review);
	}

	@Override
	public List<ReviewDto> getReviews() throws BaseException{
		List<ReviewDto> reviews = reviewDao.selectAll()
				.stream()
				.map(ReviewDto::fromEntity)
				.toList();
		return reviews;
	}
	
	@Override
	public List<ReviewDto> getReviewsByCampingId(int campingId) throws BaseException{
		Camping camping = campingDao.selectById(campingId)
				.orElseThrow(()-> new NotFoundException(BaseResponseStatus.NOT_EXIST_CAMPING));
		
		List<ReviewDto> reviews = reviewDao.selectByCampingId(camping.getId())
				.stream()
				.map(ReviewDto::fromEntity)
				.toList();
		return reviews;
	}

	@Override
	public List<ReviewDto> searchReviews(String keyword, String sido, String gugun,
			PagingAndSorting pagingAndSorting) throws BaseException{
		Integer sidoCode = (sido != null && !sido.isEmpty()) ? Integer.parseInt(sido) : null;
        Integer gugunCode = (gugun != null && !gugun.isEmpty()) ? Integer.parseInt(gugun) : null;
        
		List<Review> reviews = reviewDao.selectByCondition(keyword, sidoCode, gugunCode, pagingAndSorting);
		
		return reviews.stream()
				.map(ReviewDto::fromEntity)
				.toList();
	}
	


}
