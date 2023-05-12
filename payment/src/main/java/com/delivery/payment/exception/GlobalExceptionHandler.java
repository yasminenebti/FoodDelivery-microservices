package com.delivery.payment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    CustomException handleResourceNotFoundException(ResourceNotFoundException e ) {
        return CustomException.builder()
                .message(e.getMessage())
                .Status(HttpStatus.NOT_FOUND.value())
                .details("Resource not found")
                .timeStamp(new Date()).build();
    }

}
