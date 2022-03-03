package com.dptablo.straview.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum StraviewErrorCode {
    AUTHENTICATION_KEY_CREATION_FAILED(1000, "Authentication key creation failed.", HttpStatus.FORBIDDEN),
    INVALID_STRAVA_CLIENT_SECRET(1001, "Strava client secret is invalid.", HttpStatus.INTERNAL_SERVER_ERROR),
    STRAVA_AUTHENTICATION_TOKEN_NOT_FOUND(1002, "Strava OAuth token not found.", HttpStatus.FORBIDDEN),
    STRAVA_TOKEN_EXCHANGE_FAILED(1003, "Strava token exchange failed.", HttpStatus.INTERNAL_SERVER_ERROR),
    STRAVA_TOKEN_REFRESH_FAILED(1004, "Strava token reresh failed.", HttpStatus.INTERNAL_SERVER_ERROR),
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
