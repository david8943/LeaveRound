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

    // 회원 관련 에러
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER4001", "사용자가 없습니다."),
    MEMBER_DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER4002", "이미 사용 중인 이메일입니다."),
    MEMBER_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "MEMBER4003", "비밀번호가 일치하지 않습니다."),

    // 검증 관련 에러
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALID4001", "입력값이 유효하지 않습니다."),

    // 인증 관련 에러
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "AUTH4004", "유효 기간이 만료된 토큰입니다."),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "AUTH4005", "유효하지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.FORBIDDEN, "AUTH4006", "인증된 사용자가 아닙니다."),
    INVALID_CREDENTIALS(HttpStatus.FORBIDDEN, "AUTH4007", "인가된 사용자가 아닙니다."),

    // SSAFY API 관련 에러
    SSAFY_API_ERROR(HttpStatus.BAD_REQUEST, "SSAFY4001", "SSAFY API 요청 실패"),
    SSAFY_API_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "SSAFY4002", "SSAFY API 인증 실패"),
    SSAFY_API_CONNECTION_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "SSAFY5001", "SSAFY API 연결 실패"),

    // 자동 기부 관련 에러
    ALREADY_EXIST_AUTO_DONATION(HttpStatus.BAD_REQUEST, "AUTO4004", "이미 자동 기부에 등록된 계좌입니다."),
    NOT_FOUND_AUTO_DONATION(HttpStatus.NOT_FOUND, "AUTO4001", "존재하지 않는 자동 기부입니다."),

    // 프로젝트 관련 에러
    NOT_FOUND_ORGANIZATION_PROJECT(HttpStatus.NOT_FOUND, "PROJECT4001", "존재하지 않는 기부 프로젝트입니다."),

    // 기부 단체 관련 에러
    NOT_FOUND_ORGANIZATION(HttpStatus.NOT_FOUND, "ORGANIZATION4001", "존재하지 않는 기부 단체입니다."),

    // 민들레 관련 에러
    DANDELION_ALREADY_COLLECT(HttpStatus.BAD_REQUEST, "DANDELION4001", "이미 수집 완료된 민들레입니다."),
    DANDELION_TOO_FAR_TO_COLLECT(HttpStatus.BAD_REQUEST, "DANDELION4002", "수집할 수 있는 거리보다 멉니다."),
    DANDELION_OVER_COUNT(HttpStatus.BAD_REQUEST, "DANDELION4003", "가지고 있는 민들레 개수보다 기부 개수가 많습니다."),
    GOLD_DANDELION_OVER_COUNT(HttpStatus.BAD_REQUEST, "DANDELION4004", "가지고 있는 황금 민들레 개수보다 기부 개수가 많습니다."),
    DANDELION_EMPTY(HttpStatus.BAD_REQUEST, "DANDELION4005", "가지고 있는 민들레 개수가 없습니다."),
    GOLD_DANDELION_EMPTY(HttpStatus.BAD_REQUEST, "DANDELION4006", "가지고 잇는 황금 민들레 개수가 없습니다."),
    ORGANIZATION_EMPTY(HttpStatus.BAD_REQUEST, "DANELION4007", "지정한 기부처가 존재하지 않습니다"),
    GOLD_DANDLION_ALREADY_COLLECT(HttpStatus.BAD_REQUEST, "DANDELION4008", "이미 수집 완료된 황금 민들레입니다."),
    GOLD_DANDELION_TOO_FAR_TO_COLLECT(HttpStatus.BAD_REQUEST, "DANDELION4009", "수집할 수 있는 거리보다 멉니다");


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
