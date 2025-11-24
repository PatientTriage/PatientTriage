package com.patienttriage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

// handle the exception for the whole project
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, Object> errors = new HashMap<>();
    Map<String, String> fieldErrors = new HashMap<>();
    
    ex.getBindingResult().getFieldErrors().forEach(error -> {
      fieldErrors.put(error.getField(), error.getDefaultMessage());
    });
    
    errors.put("timestamp", java.time.Instant.now().toString());
    errors.put("status", HttpStatus.BAD_REQUEST.value());
    errors.put("error", "Validation Failed");
    errors.put("message", "Request validation failed");
    errors.put("fieldErrors", fieldErrors);
    errors.put("path", ex.getBindingResult().getTarget() != null ? 
        ex.getBindingResult().getTarget().getClass().getSimpleName() : "unknown");
    
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
    Map<String, Object> errors = new HashMap<>();
    errors.put("timestamp", java.time.Instant.now().toString());
    errors.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    errors.put("error", "Internal Server Error");
    errors.put("message", ex.getMessage());
    
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
  }
}

