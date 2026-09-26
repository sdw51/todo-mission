package com.back.except;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class globalExceptionHandler {

    //입력값 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidation(
            MethodArgumentNotValidException e
    ) {
        String message = e.getFieldError().getDefaultMessage();

        ErrorResponseDto error = new ErrorResponseDto(400, message);

        return ResponseEntity.badRequest().body(error);
    }

    // 존재하지 않는 Todo
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFound(
            ResponseStatusException e
    ) {
        ErrorResponseDto error = new ErrorResponseDto(404, e.getReason());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
