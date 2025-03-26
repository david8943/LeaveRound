package com.ssafy.Dandelion.global.apiPayload.code.status;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4002", "닉네임은 필수 입니다."),

    // ErrorStatus enum에 추가
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALID4001", "입력값이 유효하지 않습니다."),

    // 인증 관련 에러
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "AUTH4004", "유효 기간이 만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "AUTH4005", "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "AUTH4006", "인증된 사용자가 아닙니다."),
    INVALID_CREDENTIALS(HttpStatus.FORBIDDEN, "AUTH4007", "잘못된 자격 증명입니다."),

    // Ror test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),


    // 인증 관련 에러
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "AUTH4004", "유효 기간이 만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "AUTH4005", "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "AUTH4006", "인가된 사용자가 아닙니다.");
    
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
