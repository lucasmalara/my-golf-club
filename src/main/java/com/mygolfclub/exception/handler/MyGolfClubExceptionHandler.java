package com.mygolfclub.exception.handler;

import com.mygolfclub.exception.GolfClubMemberNotFoundException;
import com.mygolfclub.exception.response.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class MyGolfClubExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException exc,
                                                                               WebRequest request) {
        return new ResponseEntity<>(prepareResponse(BAD_REQUEST, exc, request), BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exc,
                                                                            WebRequest request) {
        return new ResponseEntity<>(prepareResponse(BAD_REQUEST, exc, request), BAD_REQUEST);
    }


    @ExceptionHandler(GolfClubMemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGolfClubMemberNotFoundException(GolfClubMemberNotFoundException exc,
                                                                               WebRequest request) {
        return new ResponseEntity<>(prepareResponse(NOT_FOUND, exc, request), NOT_FOUND);
    }

    private ErrorResponse prepareResponse(HttpStatus status,
                                          Exception exc,
                                          WebRequest request) {
        return ErrorResponse.builder()
                .timeStamp(LocalDateTime.now())
                .code(status.value())
                .status(status.getReasonPhrase())
                .message(exc.getMessage())
                .path(request.getDescription(false))
                .build();
    }
}
