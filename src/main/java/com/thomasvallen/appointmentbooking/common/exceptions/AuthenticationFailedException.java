package com.thomasvallen.appointmentbooking.common.exceptions;

import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends BaseException {
    public AuthenticationFailedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED.value());
    }
}
