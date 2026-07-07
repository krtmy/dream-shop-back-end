package com.helloworld.dreamshopsbackend.exception.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.rmi.AccessException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = {AccessException.class})
    public ResponseEntity<String> handleAccessDeniedException(AccessException e) {
          String message = "You do not have permission to do this type of action";
          return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }

}
