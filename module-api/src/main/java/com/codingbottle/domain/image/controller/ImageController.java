package com.codingbottle.domain.image.controller;

import com.codingbottle.domain.image.entity.Directory;
import com.codingbottle.domain.image.entity.Image;
import com.codingbottle.domain.image.model.ImageResponse;
import com.codingbottle.domain.image.restapi.ImageApi;
import com.codingbottle.domain.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController implements ImageApi {
    private final ImageService imageService;

    @Override
    public ImageResponse imageUpload(@RequestParam("directory") Directory directory,
                                                     @RequestPart("image") MultipartFile image) {
        Image upload = imageService.save(image, directory);

        return ImageResponse.from(upload);
    }
}
