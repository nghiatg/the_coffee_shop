package com.coffeeshopauthentication.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.coffeeshopauthentication.dto.response.GenericApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler 
  extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value 
      = { IllegalArgumentException.class, ClientException.class })
    protected ResponseEntity<Object> handleConflict(
      RuntimeException ex, WebRequest request) {
        GenericApiResponse errResponse = GenericApiResponse.buildErrorResponse(ex);
        return handleExceptionInternal(ex, errResponse, 
          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}