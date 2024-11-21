package com.ssafy.enjoycamping.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.ssafy.enjoycamping.common.exception.InternalServerErrorException;
import com.ssafy.enjoycamping.common.response.BaseResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class AsyncS3ImageService {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public AsyncS3ImageService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Async
    public void deleteImagesFromS3(Set<String> imagesToDelete) {
        try {
            for (String imageUrlToDelete : imagesToDelete) {
                String bucketUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/";
                String objectKey = imageUrlToDelete.substring(bucketUrl.length());
                amazonS3.deleteObject(bucket, objectKey);
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(BaseResponseStatus.FAIL_DELETE_IMAGE);
        }
    }
}
