package com.ssafy.Dandelion.global.apiPayload.code.status;

import org.springframework.http.HttpStatus;

import com.ssafy.Dandelion.global.apiPayload.code.BaseCode;
import com.ssafy.Dandelion.global.apiPayload.code.ReasonDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum SuccessStatus implements BaseCode {

	// 일반적인 응답
	_OK(HttpStatus.OK, "COMMON200", "성공입니다."),

	// 멤버 관련 응답
	SIGNUP_SUCCESS(HttpStatus.OK, "MEMBER1000", "회원가입 성공"),
	LOGIN_SUCCESS(HttpStatus.OK, "MEMBER1001", "로그인 성공"),
	USERINFO_SUCCESS(HttpStatus.OK, "MEMBER1002", "멤버 정보 조회 성공"),

	// 민들레 관련 응답
	DANDELION_LOCATION_SUCCESS(HttpStatus.OK, "DANDELION1000", "일반 민들레 위치 조회 성공"),
	GOLD_DANDELION_LOCATION_SUCCESS(HttpStatus.OK, "DANDELION1001", "황금 민들레 위치 조회 성공"),
	DANDELION_COLLECT_SUCCESS(HttpStatus.OK, "DANDELION1002", "일반 민들레 수집 성공"),
	GOLD_DANDELION_COLLECT_SUCCESS(HttpStatus.OK, "DANDELION1003", "황금 민들레 수집 성공"),
	DANDELION_DONATION_SUCCESS(HttpStatus.OK, "DANDELION1004", "민들레 기부 성공");
	// DANDELION_LOCATION_SUCCESS(HttpStatus.OK,"DANDELION1000","일반 민들레 위치 조회"),
	// DANDELION_LOCATION_SUCCESS(HttpStatus.OK,"DANDELION1000","일반 민들레 위치 조회");

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
