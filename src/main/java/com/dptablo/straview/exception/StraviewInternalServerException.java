package com.dptablo.straview.exception;

public class StraviewInternalServerException extends StraviewException {
    public StraviewInternalServerException(StraviewErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public StraviewInternalServerException(StraviewErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
