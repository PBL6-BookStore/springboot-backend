package com.pbl6.bookstore.controller;

/**
 * @author lkadai0801
 * @since 07/11/2022
 */

import com.pbl6.bookstore.common.enums.ErrorCode;
import com.pbl6.bookstore.payload.response.Response;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Log4j2
public class SecurityExceptionHandlingController {
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<?> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException exception, HttpStatus status, WebRequest request) {
        log.warn("InternalAuthenticationServiceException was risen and caught");
        log.warn("Exception: {} - {}", exception.getClass().getSimpleName(), exception.getMessage());
        log.warn("Path: {}", request.getDescription(false).substring(4));
        if (exception.getCause() != null) {
            log.warn("Cause: {} - {}", exception.getCause().getClass().getSimpleName(), exception.getCause().getMessage());
        }

        Response<?> error = Response.newBuilder()
                .setSuccess(false)
                .setMessage(exception.getMessage())
                .setErrorCode(ErrorCode.UNAUTHORIZED)
                .setException(exception.getClass().getSimpleName())
                .build();

        return ResponseEntity.status(status).body(error);
    }
}
