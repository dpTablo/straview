package com.dptablo.straview.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StraviewErrorCode {
    AUTHENTICATION_KEY_CREATION_FAILED(1000, "Authentication key creaton failed.", HttpStatus.FORBIDDEN);

    private final int errorCode;
    private final String description;
    private final HttpStatus httpStatus;

    StraviewErrorCode(int errorCode, String description, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
