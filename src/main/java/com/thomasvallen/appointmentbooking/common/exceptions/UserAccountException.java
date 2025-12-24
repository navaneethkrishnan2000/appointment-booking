package com.thomasvallen.appointmentbooking.common.exceptions;

import org.springframework.http.HttpStatus;

public class UserAccountException extends BaseException {
    public UserAccountException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }
}
