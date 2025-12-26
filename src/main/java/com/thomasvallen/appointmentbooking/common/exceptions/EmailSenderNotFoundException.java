package com.thomasvallen.appointmentbooking.common.exceptions;

import org.springframework.http.HttpStatus;

public class EmailSenderNotFoundException extends BaseException {
    public EmailSenderNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
