package com.codingbottle.s3.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.codingbottle.s3.model.MultipartFileUploadObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;


@Service
@RequiredArgsConstructor
public class AWSS3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloudfront-domain-name}")
    private String CLOUD_FRONT_DOMAIN_NAME;

    public String upload(MultipartFileUploadObject multipartFile, String fileName) {
        uploadToBucket(fileName, multipartFile.inputStream(), multipartFile.size(), multipartFile.contentType());

        return createImageUrl(fileName);
    }

    public void delete(String directory, String convertImageName) {
        amazonS3Client.deleteObject(bucket, directory + "/" + convertImageName);
    }

    private void uploadToBucket(String fileName, InputStream inputStream, long size, String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contentType);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3Client.putObject(putObjectRequest);
    }

    private String createImageUrl(String fileName){
        return CLOUD_FRONT_DOMAIN_NAME + amazonS3Client.getUrl(bucket, fileName).getPath();
    }
}
