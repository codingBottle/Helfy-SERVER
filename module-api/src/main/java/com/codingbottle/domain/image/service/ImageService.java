package com.codingbottle.domain.image.service;

import com.codingbottle.domain.image.entity.Directory;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.repo.ImageRepository;
import com.codingbottle.exception.ApplicationErrorException;
import com.codingbottle.exception.ApplicationErrorType;
import com.codingbottle.s3.model.MultipartFileUploadObject;
import com.codingbottle.s3.service.AWSS3Service;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final AWSS3Service awss3Service;

    @SneakyThrows
    @Transactional
    public Image save(MultipartFile multipartFile, Directory directory) {
        validateImage(multipartFile.getContentType());

        String fileName = createFileName(multipartFile.getOriginalFilename(), directory.getName());

        String imageUrl = uploadMultipartFile(multipartFile, fileName);

        Image image = Image.builder()
                .directory(directory)
                .imageUrl(imageUrl)
                .convertImageName(fileName.substring(fileName.lastIndexOf("/") + 1))
                .build();

        return saveImage(image);
    }

    @Transactional(readOnly = true)
    public Image findById(Long imageId) {
        return imageRepository.findById(imageId).orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.IMAGE_NOT_FOUND, "해당 이미지를 찾을 수 없습니다."));
    }

    @Transactional
    public void delete(Image image) {
        awss3Service.delete(image.getDirectory().getName(), image.getConvertImageName());
        imageRepository.deleteById(image.getId());
    }

    public void deleteByImageUrl(String imageUrl) {
        Image image = imageRepository.findByImageUrl(imageUrl).orElseThrow(() -> new ApplicationErrorException(ApplicationErrorType.IMAGE_NOT_FOUND, "해당 이미지를 찾을 수 없습니다."));
        awss3Service.delete(image.getDirectory().getName(), image.getConvertImageName());
        imageRepository.delete(image);
    }

    private String uploadMultipartFile(MultipartFile multipartFile, String fileName) throws IOException {
        MultipartFileUploadObject multipartFileUploadObject = MultipartFileUploadObject.of(multipartFile.getInputStream(), multipartFile.getSize(), multipartFile.getContentType());

        return awss3Service.upload(multipartFileUploadObject, fileName);
    }

    private Image saveImage(Image image){
        return imageRepository.save(image);
    }

    private String createFileName(String fileName, String dirName) {
        return dirName + "/" + UUID.randomUUID() + "_" + fileName;
    }

    private void validateImage(String contentType) {
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new ApplicationErrorException(ApplicationErrorType.INVALID_FILE_TYPE);
        }
    }
}
