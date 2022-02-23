package com.dptablo.straview.exception;

public class AuthenticationException extends StraviewException {
    public AuthenticationException(StraviewErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
