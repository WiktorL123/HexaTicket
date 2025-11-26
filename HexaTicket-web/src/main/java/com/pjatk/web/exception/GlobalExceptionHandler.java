package com.pjatk.web.exception;

import com.pjatk.core.exception.NotFoundException;
import com.pjatk.core.exception.TooEarlyDateException;
import com.pjatk.core.exception.TooMuchSeatsException;
import org.springframework.data.mongodb.core.mapping.FieldName;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception){
        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error->{
            String fieldName = ((FieldError) error).getField();
            String errorName = error.getDefaultMessage();
            errors.put(fieldName, errorName);
        });
        return ResponseEntity.badRequest().body(new ErrorResponse(errors));
    }
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse>handleNotFoundException(NotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(Map.of("Erorr", exception.getMessage())));
    }
    @ExceptionHandler(TooEarlyDateException.class)
    public ResponseEntity<ErrorResponse>handleTooEarlyDateException(TooEarlyDateException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(Map.of("Error: ", e.getMessage() )));
    }
    @ExceptionHandler(TooMuchSeatsException.class)
    public ResponseEntity<ErrorResponse>handleTooMuchSeatsException(TooMuchSeatsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(Map.of("Error: ", e.getMessage())));
    }

}
