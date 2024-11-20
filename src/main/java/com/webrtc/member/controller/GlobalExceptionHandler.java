package com.webrtc.member.controller;


import com.webrtc.member.exception.JWTValidationException;
import com.webrtc.member.exception.KakaoOAuth2Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(KakaoOAuth2Exception.class)
    public ResponseEntity<String> handleKakaoOAuth2Exception(KakaoOAuth2Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(JWTValidationException.class)
    public ResponseEntity<String> handleJWTValidationException(JWTValidationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}
