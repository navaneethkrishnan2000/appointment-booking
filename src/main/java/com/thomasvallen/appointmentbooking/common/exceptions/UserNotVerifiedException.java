package com.thomasvallen.appointmentbooking.common.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotVerifiedException extends BaseException {
    public UserNotVerifiedException(String message) {
        super(message, HttpStatus.FORBIDDEN.value());
    }
}
