package com.codingbottle.domain.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.codingbottle.common.exception.ApplicationErrorException;
import com.codingbottle.domain.image.entity.Directory;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.codingbottle.common.exception.ApplicationErrorType.*;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3Client amazonS3Client;
    private final ImageRepository imageRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    @Value("${cloudfront-domain-name}")
    private String CLOUD_FRONT_DOMAIN_NAME;



    @Transactional
    public Image upload(MultipartFile multipartFile, Directory directory) {
        validateImage(multipartFile.getContentType());

        String fileName = createFileName(multipartFile.getOriginalFilename(), directory.getName());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            uploadToBucket(fileName, inputStream, multipartFile.getSize(), multipartFile.getContentType());

            Image image = createImageEntity(fileName, directory);

            return saveImage(image);
        } catch (IOException e) {
            throw new ApplicationErrorException(FAIL_TO_UPLOAD_IMAGE, "파일 업로드 중 오류가 발생했습니다.");
        }
    }

    @Transactional(readOnly = true)
    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new ApplicationErrorException(IMAGE_NOT_FOUND, "해당 이미지를 찾을 수 없습니다."));
    }

    @Transactional
    public void delete(Image image) {
        amazonS3Client.deleteObject(bucket, image.getDirectory().getName() + "/" + image.getConvertImageName());

        imageRepository.deleteById(image.getId());
    }



    private void uploadToBucket(String fileName, InputStream inputStream,
                                long size,String contentType) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contentType);

        PutObjectRequest putObjectRequest =
                new PutObjectRequest(bucket, fileName,inputStream ,objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead);

        amazonS3Client.putObject(putObjectRequest);
    }

    private Image createImageEntity(String fileName,Directory directory){
        String path = amazonS3Client.getUrl(bucket, fileName).getPath();
        return Image.builder()
                .imageUrl(CLOUD_FRONT_DOMAIN_NAME + path)
                .directory(directory)
                .convertImageName(fileName.substring(fileName.lastIndexOf("/") + 1))
                .build();
    }

    private Image saveImage(Image image){
        return imageRepository.save(image);
    }

    private String createFileName(String fileName, String dirName) {
        return dirName + "/" + UUID.randomUUID() + "_" + fileName;
    }

    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApplicationErrorException(INVALID_FILE_TYPE);
        }
    }
}
