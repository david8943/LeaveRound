package com.ssafy.Dandelion.global.apiPayload.exception.handler;

import com.ssafy.Dandelion.global.apiPayload.code.BaseErrorCode;
import com.ssafy.Dandelion.global.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {
    public MemberHandler(BaseErrorCode code) {
        super(code);
    }
}
