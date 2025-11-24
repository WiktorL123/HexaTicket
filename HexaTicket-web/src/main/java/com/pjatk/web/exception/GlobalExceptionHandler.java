package com.pjatk.web.exception;

import org.springframework.data.mongodb.core.mapping.FieldName;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error->{
            String fieldName = ((FieldError) error).getField();
            String errorName = error.getDefaultMessage();
            errors.put(fieldName, errorName);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
