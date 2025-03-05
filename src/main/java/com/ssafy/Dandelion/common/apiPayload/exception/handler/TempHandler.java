package com.ssafy.Dandelion.common.apiPayload.exception.handler;

import com.ssafy.Dandelion.common.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.common.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
