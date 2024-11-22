package com.ssafy.enjoycamping.review.service;


import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.ssafy.enjoycamping.common.service.AsyncS3ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.ssafy.enjoycamping.common.exception.BaseException;
import com.ssafy.enjoycamping.common.exception.NotFoundException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.dao.ReviewImageDao;
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
	private final ReviewImageDao reviewImageDao;
	private final UserDao userDao;
	private final CampingDao campingDao;
	private final AmazonS3 amazonS3;
	@Value("${aws.s3.bucket}")
	private String bucket;
	private final AsyncS3ImageService asyncS3ImageService;
	
	@Transactional
	@Override
	public CreateReviewDto.ResponseCreateReviewDto createReview(CreateReviewDto.RequestCreateReviewDto request) {
//		int id = JwtProvider.getUserId();
//		// JWT로 User 불러오기 //access Token 만료됐는지 확인하기
//		User user = userDao.selectActiveById(id)
//				.orElseThrow(() -> new UnauthorizedException(BaseResponseStatus.INVALID_USER_JWT));
		Camping camping = campingDao.selectById(request.getCampingId())
				.orElseThrow(()->new NotFoundException(BaseResponseStatus.NOT_EXIST_CAMPING));
		
		Review newReview = request.toEntity(camping,6); //TODO: JWT로 ID정보 가져올 것
		reviewDao.insert(newReview);

		//이미지 맵핑 테이블에 이미지 URL저장
		Set<String> imageUrls = Optional.ofNullable(request.getImageUrls()).orElse(Collections.emptySet());
		if (!imageUrls.isEmpty()) reviewImageDao.insert(newReview.getId(), imageUrls);

		return CreateReviewDto.ResponseCreateReviewDto.builder()
				.id(newReview.getId())
				.build();
	}
	
	@Override
	public URL createImageUrl(String fileName, String contentType) throws IOException {
		// S3 객체 키 (파일 이름)
		String objectKey = "uploads/" + fileName;
		Date expireTime = Date.from(Instant.now().plus(1,ChronoUnit.MINUTES));

		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, objectKey)
				.withMethod(HttpMethod.PUT)
				.withContentType(contentType)
				.withExpiration(expireTime);

		// 'x-amz-acl' 헤더를 'public-read'로 설정하여 퍼블릭 읽기 권한 부여
		request.addRequestParameter("x-amz-acl", "public-read");

		//preSignedUrl 발급
		return amazonS3.generatePresignedUrl(request);
	}

	@Override
	public ReviewDto getReview(int id) throws BaseException {
		Review review = reviewDao.selectById(id)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_REVIEW));
		return ReviewDto.fromEntity(review);
	}

	@Transactional
	@Override
	public void deleteReview(int id) {
		//TODO: 로그인 유저와 작성자 확인 후 맞으면 삭제하는 로직
		Review review = reviewDao.selectById(id)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_REVIEW));
		
		Set<String> ImageUrlsToDelete = reviewImageDao.selectImageUrlsByReviewId(id);
		if(!ImageUrlsToDelete.isEmpty()) {
			reviewImageDao.delete(ImageUrlsToDelete);
			asyncS3ImageService.deleteImagesFromS3(ImageUrlsToDelete);
		}
		reviewDao.delete(id);
		log.info("삭제 완료");
	}

	@Transactional
	@Override
	public ReviewDto updateReview(UpdateReviewDto.RequestUpdateReviewDto request, int id) {
		//TODO: 로그인 유저와 작성자 확인 후 맞으면 업데이트하는 로직
		Review review = reviewDao.selectById(id)
				.orElseThrow(() -> new NotFoundException(BaseResponseStatus.NOT_EXIST_REVIEW));
		
		//새로운 이미지 URL
		Set<String> newReviewImages = Optional.ofNullable(request.getImageUrls()).orElse(new HashSet<>());
		//기존 이미지 URL
		Set<String> originReviewImages = reviewImageDao.selectImageUrlsByReviewId(id);
		
		//추가된 이미지
		Set<String>imagesToInsert = new HashSet<>(newReviewImages);
		imagesToInsert.removeAll(originReviewImages);
		
		//삭제될 이미지
		Set<String>imagesToDelete = new HashSet<>(originReviewImages);
		imagesToDelete.removeAll(newReviewImages);
		
		//추가된 이미지가 있다면 추가
		if(!imagesToInsert.isEmpty()) {
			reviewImageDao.insert(id, imagesToInsert);
		}
		//삭제될 이미지가 있다면 삭제
		if(!imagesToDelete.isEmpty()) {
			//S3 이미지 삭제
			reviewImageDao.delete(imagesToDelete);
			asyncS3ImageService.deleteImagesFromS3(imagesToDelete);
		}
		
		review.updateReview(request);
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
