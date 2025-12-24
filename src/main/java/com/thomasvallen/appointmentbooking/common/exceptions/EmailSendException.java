package com.thomasvallen.appointmentbooking.common.exceptions;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message) {
        super(message);
    }
}

