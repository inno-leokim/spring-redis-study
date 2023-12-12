package com.brand13.redis.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.brand13.redis.vo.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExController {
    
    // @ExceptionHandler({Exception.class}) //Exception.class를 지정하면 전체 예외처리를 다 받게 된다.
    // public ResponseEntity<Object> EveryException(final Exception ex){
    //     return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    // }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> NotFoundExceptionResponse(NotFoundException e){
        return new ResponseEntity<>(e.getErrmsg(), e.getHttpStatus());
    }
}
