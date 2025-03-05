package com.ssafy.Dandelion.globbal.apiPayload.exception.handler;

import com.ssafy.Dandelion.globbal.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.globbal.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
