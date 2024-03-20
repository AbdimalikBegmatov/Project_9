package com.example.project_9.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomMethodArgumentNotValidException {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handlingArgumentNotValid(MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)->{
            errors.put(((FieldError) error).getField(),error.getDefaultMessage());
        });

        return new ResponseEntity<>(
                new ApiError(
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now(),
                        errors),
                HttpStatus.BAD_REQUEST
        );
    }
 }
