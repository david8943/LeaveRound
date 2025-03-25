package com.ssafy.Dandelion.global.apiPayload.exception.handler;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;

public class ForbiddenHandler extends GeneralException {

    public ForbiddenHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
