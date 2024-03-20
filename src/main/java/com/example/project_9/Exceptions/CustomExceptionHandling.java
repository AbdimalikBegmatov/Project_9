package com.example.project_9.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomExceptionHandling {
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ApiError> handlingCustomException(CustomException ex){

        Map<String,String> error = new HashMap<>();

        error.put("error", ex.getMessage());

        return new ResponseEntity<>(
                new ApiError(
                    HttpStatus.BAD_REQUEST.value(),
                    LocalDateTime.now(),
                    error
        ),HttpStatus.BAD_REQUEST);
    }
}
