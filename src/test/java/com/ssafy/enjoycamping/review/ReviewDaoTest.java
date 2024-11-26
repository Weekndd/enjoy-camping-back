package com.ssafy.enjoycamping.review;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ssafy.enjoycamping.common.util.PagingAndSorting;
import com.ssafy.enjoycamping.common.util.PagingAndSorting.ReviewOrder;
import com.ssafy.enjoycamping.common.util.PagingAndSorting.Sort;
import com.ssafy.enjoycamping.review.dao.ReviewDao;
import com.ssafy.enjoycamping.review.entity.Review;

@SpringBootTest
class ReviewDaoTest {
	@Autowired
	ReviewDao reviewDao;

	@Test
	@DisplayName("조건으로 리뷰 검색")
	void selectByCondition() {
		//GIVEN
		String keyword = "test";
		int sidoCode = 32;
		int gugunCode = 13;
		ReviewOrder order = ReviewOrder.created_at;
		Sort sort = Sort.asc;
		PagingAndSorting pagingAndSorting = new PagingAndSorting(1, 9, order, sort);
		//WHEN
		List<Review> reviews = reviewDao.selectByCondition(keyword, sidoCode, gugunCode, pagingAndSorting);
		//THEN
		System.out.println(reviews.size());
		int expectedSize = 7;
		int actualSize = reviews.size();
		assertTrue(expectedSize==actualSize,"예상값과 실제값이 같습니다.");

	}

}
