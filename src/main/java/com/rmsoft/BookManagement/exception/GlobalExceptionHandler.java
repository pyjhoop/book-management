package com.rmsoft.BookManagement.exception;

import com.rmsoft.BookManagement.dto.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Api<?>> handleRuntimeException(RuntimeException e){

        return ResponseEntity.status(400)
                .body(Api.builder()
                        .code(400)
                        .status("Bad Request")
                        .message(e.getMessage())
                        .build());
    }
}
