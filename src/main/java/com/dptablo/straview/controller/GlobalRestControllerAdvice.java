package com.dptablo.straview.controller;

import com.dptablo.straview.exception.StraviewException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = { "com.dptablo.straview.controller" })
@Slf4j
public class GlobalRestControllerAdvice {
    @ExceptionHandler(StraviewException.class)
    public ResponseEntity<String> straviewExceptionHandler(StraviewException e) {
        return defaultExceptionProcess(e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception e) {
        return defaultExceptionProcess(e);
    }

    private ResponseEntity<String> defaultExceptionProcess(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
