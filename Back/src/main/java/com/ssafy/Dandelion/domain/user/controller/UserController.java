package com.ssafy.Dandelion.domain.user.controller;

import com.ssafy.Dandelion.domain.user.dto.request.UserLoginRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.request.UserSignUpRequestDTO;
import com.ssafy.Dandelion.domain.user.dto.response.UserInfoResponseDTO;
import com.ssafy.Dandelion.domain.user.service.UserService;
import com.ssafy.Dandelion.global.apiPayload.ApiResponse;
import com.ssafy.Dandelion.global.apiPayload.code.status.SuccessStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    /**
     * 회원가입
     *
     * @param userSignUpRequestDTO 회원가입 요청 데이터 (이름, 이메일, 비밀번호)
     * @return 성공 응답
     */
    @PostMapping("")
    public ApiResponse<Void> signUp(@Valid @RequestBody UserSignUpRequestDTO userSignUpRequestDTO) {
        userService.signup(userSignUpRequestDTO);
        return ApiResponse.of(SuccessStatus.SIGNUP_SUCCESS, null);
    }

    /**
     * 로그인
     *
     * @param userLoginRequestDTO 로그인 요청 데이터 (이메일, 비밀번호)
     * @return 성공 응답
     */
    @PostMapping("/login")
    public ApiResponse<Void> login(@Valid @RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        // 실제 인증 로직은 LoginFilter에서 처리됨
        return ApiResponse.of(SuccessStatus.LOGIN_SUCCESS, null);
    }

    /**
     * 회원 정보 조회 API
     *
     * @param userId 조회할 회원의 ID
     * @return 회원 상세 정보
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserInfoResponseDTO> getUserInfo(@PathVariable Integer userId) {
        UserInfoResponseDTO userInfo = userService.getUserInfo(userId);
        return ApiResponse.of(SuccessStatus.USERINFO_SUCCESS, userInfo);
    }

}
