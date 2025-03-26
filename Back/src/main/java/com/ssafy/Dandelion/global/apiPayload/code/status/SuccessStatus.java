package com.ssafy.Dandelion.global.apiPayload.code.status;

import com.ssafy.Dandelion.global.apiPayload.code.BaseCode;
import com.ssafy.Dandelion.global.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor

public enum SuccessStatus implements BaseCode {

    // 일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    // 멤버 관련 응답
    SIGNUP_SUCCESS(HttpStatus.OK, "1000", "회원가입 성공"),
    LOGIN_SUCCESS(HttpStatus.OK, "1001", "로그인 성공"),
    USERINFO_SUCCESS(HttpStatus.OK, "1002", "멤버 정보 조회 성공");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
