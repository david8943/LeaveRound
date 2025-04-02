package com.ssafy.Dandelion.global.apiPayload.exception.handler;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;

public class DandelionHandler extends GeneralException {
	public DandelionHandler(BaseErrorCode errorCode) {
		super(errorCode);
	}
}
