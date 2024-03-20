package com.example.project_9.Exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ApiError {
    private Integer httpStatus;
    private LocalDateTime dateTime;
    private Map<String,String> errors;

    public ApiError(Integer httpStatus, LocalDateTime dateTime, Map<String, String> errors) {
        this.httpStatus = httpStatus;
        this.dateTime = dateTime;
        this.errors = errors;
    }
}
