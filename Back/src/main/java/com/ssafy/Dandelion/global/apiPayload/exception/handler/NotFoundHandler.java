package com.ssafy.Dandelion.global.apiPayload.exception.handler;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;

public class NotFoundHandler extends GeneralException {

	public NotFoundHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
