package com.simpleblogapi.simpleblogapiv2.exceptions;

import com.simpleblogapi.simpleblogapiv2.dtos.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<ApiResponse<?>> handleRuntimeException(RuntimeException e) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.UNCAUGHT_EXCEPTION.getCode())
                .message(ErrorCode.UNCAUGHT_EXCEPTION.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.UNCAUGHT_EXCEPTION.getStatusCode()).body(response);
    }

    @ExceptionHandler(AppException.class)
    ResponseEntity<ApiResponse<?>> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid key");
        }
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }

    @ExceptionHandler(IOException.class)
    ResponseEntity<ApiResponse<?>> handleIOException(IOException e) {
        ApiResponse<?> response = ApiResponse.builder()
                .code(ErrorCode.FILE_ERROR.getCode())
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(ErrorCode.FILE_ERROR.getStatusCode()).body(response);
    }


    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.FORBIDDEN;
        ApiResponse<?> response = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.getStatusCode()).body(response);
    }
}
