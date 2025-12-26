package com.thomasvallen.appointmentbooking.common.exceptions;

import org.springframework.http.HttpStatus;

public class NewsletterSubscriberNotFoundException extends BaseException {
    public NewsletterSubscriberNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND.value());
    }
}
