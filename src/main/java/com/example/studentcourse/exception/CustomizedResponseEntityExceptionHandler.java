package com.example.studentcourse.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleMethodValid(ConstraintViolationException ex){
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage).toList();


        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST,errors.get(0), ex.getConstraintViolations().toString()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public final ResponseEntity<Object> handeSQLVialationException(SQLIntegrityConstraintViolationException exception) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                LocalDateTime.now(), HttpStatus.BAD_REQUEST,exception.getMessage(), exception.toString()
        );
        return ResponseEntity.badRequest().body(exceptionResponse);
    }

//    @Override
//    public final ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//
//        BindingResult result = exception.getBindingResult();
//        List<FieldError> fieldErrors = result.getFieldErrors();
//
//        // Create a response containing the error details
//        StringBuilder errorMessage = new StringBuilder();
//        for (FieldError error : fieldErrors) {
//            errorMessage
//                    .append(error.getDefaultMessage())
//                    .append("; ");
//        }
//        ExceptionResponse exceptionResponse = new ExceptionResponse(
//                LocalDateTime.now(),""
//                + errorMessage, exception.getBindingResult().toString()
//        );
//
//        return ResponseEntity.badRequest().body(exceptionResponse);
//    }
}
