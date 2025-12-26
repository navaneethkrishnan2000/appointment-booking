package com.thomasvallen.appointmentbooking.common.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateSubscriptionException extends BaseException {
    public DuplicateSubscriptionException(String message) {
        super(message, HttpStatus.CONFLICT.value());
    }
}
