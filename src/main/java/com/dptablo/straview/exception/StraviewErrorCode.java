package com.dptablo.straview.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StraviewErrorCode {
    AUTHENTICATION_KEY_CREATION_FAILED(1000, "Authentication key creaton failed.", HttpStatus.FORBIDDEN),
    INVALID_STRAVA_CLIENT_SECRET(1001, "Strava client secret is invalid.", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVER_PROCESSION_ERROR(5000, "Error occurred during server processing.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int errorCode;
    private final String description;
    private final HttpStatus httpStatus;

    StraviewErrorCode(int errorCode, String description, HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
