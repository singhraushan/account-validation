package com.rau.jp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class AccountExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
  public ResponseEntity<String> handleCustomException(CustomException ex) {
      return new ResponseEntity<String>(ex.getMessage(), ex.getStatus());
  }

  @ExceptionHandler(value = Exception.class)
  public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<String>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}