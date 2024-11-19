package com.ssafy.enjoycamping.review.service;


import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.NotFoundException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dto.CreateReviewDto;
import com.ssafy.enjoycamping.review.dto.ReviewDto;
import com.ssafy.enjoycamping.review.dto.UpdateReviewDto;
import com.ssafy.enjoycamping.review.entity.Review;
import com.ssafy.enjoycamping.review.entity.ReviewImage;
import com.ssafy.enjoycamping.trip.camping.dao.CampingDao;
import com.ssafy.enjoycamping.trip.camping.entity.Camping;
import com.ssafy.enjoycamping.user.dao.UserDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewDao reviewDao;
	private final UserDao userDao;
	private final CampingDao campingDao;
	private final AmazonS3 amazonS3;
	
	@Value("${aws.s3.bucket}")
	private String bucket;

	@Override
	public CreateReviewDto.ResponseCreateReviewDto createReview(CreateReviewDto.RequestCreateReviewDto request) {
//		int id = JwtProvider.getUserId();
//		// JWT로 User 불러오기 //access Token 만료됐는지 확인하기
//		User user = userDao.selectActiveById(id)
//				.orElseThrow(() -> new UnauthorizedException(BaseResponseStatus.INVALID_USER_JWT));
		
		Review newReview = request.toEntity();
		reviewDao.insert(newReview);
		
		//이미지 맵핑 테이블에 이미지 URL저장
		List<ReviewImage> reviewImages = request.getImageUrls().stream()
				.map(url -> ReviewImage.from(newReview.getId(), url))
				.toList();
		reviewDao.insertImages(reviewImages);
		
		return CreateReviewDto.ResponseCreateReviewDto.builder()
				.id(newReview.getId())
				.build();
	}
	
	@Override
	public URL createImageUrl(MultipartFile image) throws IOException{
		String s3FileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
		//preSignedUrl 발급
		Date expireTime = Date.from(Instant.now().plus(2,ChronoUnit.HOURS));
		URL preSignedUrl = amazonS3.generatePresignedUrl(bucket, s3FileName, expireTime, HttpMethod.PUT);
		return preSignedUrl;
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
		
		Set<String> newReviewImages = request.getImageUrls();
		Set<String> originReviewImages = reviewDao.selectAllImageUrl(id);
		newReviewImages.removeAll(originReviewImages);
//		reviewDao.insertImages(newReviewImages.stream().map(url -> ReviewImage.from(id, url)).toList()); //추가할 이미지
		System.out.println("========이미지 수 : "+newReviewImages.size());
		newReviewImages = request.getImageUrls();
		System.out.println("========다시 받은 이미지 수 : "+newReviewImages.size());
		
//		request.updateReview(review);
//		
//		reviewDao.update(review);
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
	public List<ReviewDto> getReviewsByCondition(String keyword, String sido, String gugun,
			PagingAndSorting pagingAndSorting) throws BaseException {
		Integer sidoCode = (sido != null && !sido.isEmpty()) ? Integer.parseInt(sido) : null;
        Integer gugunCode = (gugun != null && !gugun.isEmpty()) ? Integer.parseInt(gugun) : null;
        
		List<Review> reviews = reviewDao.selectByCondition(keyword, sidoCode, gugunCode, pagingAndSorting);
		
		return reviews.stream()
				.map(ReviewDto::fromEntity)
				.toList();
	}
	
	@Override
	public List<ReviewDto> getReviewsByUserId(int userId) {
		List<Review> reviews = reviewDao.selectByUserId(userId);
		return reviews.stream()
				.map(ReviewDto::fromEntity)
				.toList();
	}
	
	


}
