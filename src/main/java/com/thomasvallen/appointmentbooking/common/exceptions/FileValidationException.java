package com.thomasvallen.appointmentbooking.common.exceptions;

import org.springframework.http.HttpStatus;

public class FileValidationException extends BaseException {
    public FileValidationException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
