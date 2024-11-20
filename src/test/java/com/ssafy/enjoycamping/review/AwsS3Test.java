package com.ssafy.enjoycamping.review;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

@SpringBootTest
class AwsS3Test {
	
	@Autowired
	private AmazonS3 amazonS3; //config파일에서 @Bean설정해준 AmazonS3Client객체는 AmazonS3를 상속받고 있음
	
	@Test
	@DisplayName("AwsS3 연결 테스트")
	void AwsS3ConnectionTest() {
		List<Bucket> buckets = amazonS3.listBuckets();
		
		System.out.println("S3 버킷 목록:");
        for (Bucket bucket : buckets) {
            System.out.println("- " + bucket.getName());
        }
        
        // 버킷 목록이 비어 있더라도 오류가 없다면 연결이 성공한 것입니다.
        assertNotNull(buckets,"S3성공");
	}

}
