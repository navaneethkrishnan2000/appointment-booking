package com.thomasvallen.appointmentbooking.common.exceptions;

public class FileValidationException extends RuntimeException {
    public FileValidationException(String message) {
        super(message);
    }
}
