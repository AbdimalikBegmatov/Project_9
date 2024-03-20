package com.example.project_9.Exceptions;

public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
