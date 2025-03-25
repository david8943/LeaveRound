package com.ssafy.Dandelion.global.apiPayload.exception.handler;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;

public class SsafyApiHandler extends GeneralException {
    public SsafyApiHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
