package com.dptablo.straview.exception;

import lombok.Getter;

public class StraviewException extends Exception {
    @Getter
    private final StraviewErrorCode errorCode;

    public StraviewException(StraviewErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
