package com.ssafy.Dandelion.common.apiPayload.code;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}