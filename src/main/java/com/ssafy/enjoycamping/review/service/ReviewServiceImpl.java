package com.ssafy.enjoycamping.review.service;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
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

import lombok.AllArgsConstructor;
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
		
		//미리 저장된 맵핑테이블의 이미지에 ReviewId부여
		List<String> imageUrls = request.getImageUrls();
		for(int i=0; i<imageUrls.size(); i++) {
			ReviewImage reviewImage = ReviewImage.builder()
					.reviewId(newReview.getId())
					.imageUrl(imageUrls.get(i))
					.build();
			reviewDao.updateImageReviewId(reviewImage);
		}
		return CreateReviewDto.ResponseCreateReviewDto.builder()
				.id(newReview.getId())
				.build();
	}
	
	@Override
	public String createImageUrl(MultipartFile image) throws IOException{
		String s3FileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(image.getContentType()); //업로드된 파일의 MIME 타입을 설정
		metadata.setContentLength(image.getSize()); //S3는 파일 업로드 전에 크기를 알야하기 떄문에 설정해야함
		amazonS3.putObject(bucket, s3FileName, image.getInputStream(), metadata);
		String imageUrl = amazonS3.getUrl(bucket, s3FileName).toString();
		
		//이미지 맵핑 테이블에 추가
		reviewDao.insertImage(ReviewImage.builder()
				.imageUrl(imageUrl)
				.build());
		
		return imageUrl;
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
