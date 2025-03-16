package com.ssafy.Dandelion.global.apiPayload.exception.handler;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;

public class AuthHandler extends GeneralException {

    public AuthHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
