package com.ssafy.enjoycamping.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {
	@Value("${aws.s3.accessKey}")
	private String accessKey;
	
	@Value("${aws.s3.secretKey}")
	private String secretKey;
	
	@Value("${aws.s3.region}")
	private String region;
	
	@Bean
	public AmazonS3Client amazonS3Client() { //AmazonS3Client는 AWS와 상호작용하는 객체
	      BasicAWSCredentials awsCredentials= new BasicAWSCredentials(accessKey, secretKey);
	      return (AmazonS3Client)AmazonS3ClientBuilder.standard()
	         .withRegion(region)
	         .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
	         .build();
	   }
	
}
