package com.ssafy.Dandelion.global.apiPayload.exception.handler;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;

public class BadRequestHandler extends GeneralException {

	public BadRequestHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
