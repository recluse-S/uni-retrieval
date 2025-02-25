package com.retrieval.handler;

import com.retrieval.pojo.Result;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Result> handleExpiredJwtException(ExpiredJwtException e) {
        return new ResponseEntity<>(Result.error("JWT已过期，请重新登录"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleException(Exception e) {
        return new ResponseEntity<>(Result.error("系统错误"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
