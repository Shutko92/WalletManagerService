package com.example.depositeService.controller.advice;

import com.example.depositeService.exception.ErrorResponse;
import com.example.depositeService.exception.ExceptionMessage;
import com.example.depositeService.exception.UserNotFoundException;
import com.example.depositeService.exception.WalletNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class WalletControllerAdvice {

    @ExceptionHandler({HttpMessageNotReadableException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(ExceptionMessage.BINDING_RESULTS_ERROR.getMessage(), HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WalletNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleMethodException(MethodArgumentNotValidException e, WebRequest request) {
        List<FieldError> errors = e.getFieldErrors();
        String message = errors.stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .filter(StringUtils::isNotEmpty)
                .collect(Collectors.joining(", "));
        ErrorResponse errorResponse = new ErrorResponse(message, HttpStatus.BAD_REQUEST, request);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
