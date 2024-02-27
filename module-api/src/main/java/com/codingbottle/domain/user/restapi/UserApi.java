package com.codingbottle.domain.user.restapi;

import com.codingbottle.common.annotation.DefaultApiResponse;
import com.codingbottle.domain.user.entity.User;
import com.codingbottle.domain.user.model.UserInfoUpdateRequest;
import com.codingbottle.domain.user.model.UserProfileImageUpdateRequest;
import com.codingbottle.domain.user.model.UserResponse;
import com.codingbottle.domain.user.model.UserResponseWithRankInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 정보", description = "사용자 정보 API")
@DefaultApiResponse
@RestController
@RequestMapping("/api/v1/user")
public interface UserApi {
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회합니다.")
    @GetMapping
    UserResponseWithRankInfo getUser(
            User user
    );

    @Operation(summary = "사용자 정보 수정", description = "사용자 정보를 수정합니다. (닉네임, 지역)")
    @PatchMapping
    UserResponse updateUserInfo(
            User user,
            @RequestBody @Validated UserInfoUpdateRequest userInfoUpdateRequest
    );

    @Operation(summary = "사용자 프로필 이미지 변경", description = "사용자 프로필 이미지를 변경합니다.")
    @PatchMapping("/image")
    UserResponse updateProfileImage(
            User user,
            @RequestBody @Validated UserProfileImageUpdateRequest userProfileImageUpdateRequest
    );
}
