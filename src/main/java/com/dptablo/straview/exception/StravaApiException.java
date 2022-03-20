package com.dptablo.straview.exception;

public class StravaApiException extends StraviewException {
    public StravaApiException(StraviewErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
