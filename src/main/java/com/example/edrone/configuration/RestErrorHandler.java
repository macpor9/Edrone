package com.example.edrone.configuration;

import com.example.edrone.dto.response.ErrorResponse;
import com.example.edrone.exception.NumberOfStringsExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.Timestamp;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(NumberOfStringsExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object processValidationError(NumberOfStringsExceededException ex) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .build()
        );
    }

}