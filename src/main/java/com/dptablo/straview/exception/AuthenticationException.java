package com.dptablo.straview.exception;

public class AuthenticationException extends StraviewException {
    public AuthenticationException(StraviewErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public AuthenticationException(StraviewErrorCode errorCode) {
        super(errorCode, errorCode.getDescription());
    }

    public AuthenticationException(StraviewErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
