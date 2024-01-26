package com.codingbottle.domain.image.controller;

import com.codingbottle.domain.image.entity.Directory;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.image.service.ImageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지", description = "이미지 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(value = "/images", consumes = "multipart/form-data")
    public ResponseEntity<ImageResponse> imageUpload(@RequestParam("directory") Directory directory,
                                                     @RequestPart("image") MultipartFile image) {
        Image upload = imageService.save(image, directory);

        return ResponseEntity.ok(ImageResponse.of(upload));
    }
}
