package com.example.project_9.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomNotFoundExceptionHandling {
    @ExceptionHandler({CustomNotFoundException.class})
    public ResponseEntity<ApiError> handlingCustomException(CustomNotFoundException ex){

        Map<String,String> error = new HashMap<>();

        error.put("error", ex.getMessage());

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.NOT_FOUND.value(),
                        LocalDateTime.now(),
                        error
                ),HttpStatus.NOT_FOUND);
    }
}
