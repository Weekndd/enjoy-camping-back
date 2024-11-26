package com.ssafy.enjoycamping.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Set;

@EnableAsync
@Configuration
public class S3Config {
	@Value("${aws.s3.accessKey}")
	private String accessKey;
	
	@Value("${aws.s3.secretKey}")
	private String secretKey;
	
	@Value("${aws.s3.region}")
	private String region;
	
	//AmazonS3Client는 AWS와 상호작용하는 객체
	//가끔 에러가 발생하는데 AmazonS3로 하면 될 때가 있다고 함
	// ->Bean생성될 때 메서드의 이름으로 생성 되어서 메서드 이름과 사용할 객체의 이름을 맞춰서 해야한다고 함
	@Bean
	public AmazonS3Client amazonS3Client() { 
		BasicAWSCredentials awsCredentials= new BasicAWSCredentials(accessKey, secretKey);
		return (AmazonS3Client)AmazonS3ClientBuilder.standard()
		 .withRegion(region)
		 .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
		 .build();
	}
	
}
