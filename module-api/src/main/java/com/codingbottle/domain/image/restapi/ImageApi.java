package com.codingbottle.domain.image.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.image.entity.Directory;
import com.codingbottle.domain.image.model.ImageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지", description = "이미지 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1")
public interface ImageApi {
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드합니다.")
    @PostMapping(value = "/images", consumes = "multipart/form-data")
    ImageResponse imageUpload(
            @RequestParam("directory") @Parameter(example = "POST", description = "어떤 용도에서 사용할 이미지인지 정합니다. (POST, INFORMATION, QUIZ, USER)") Directory directory,
            @RequestPart("image") @Parameter(description = "이미지 파일을 업로드합니다. PNG, JPEG 만 가능합니다.") MultipartFile image
    );
}
