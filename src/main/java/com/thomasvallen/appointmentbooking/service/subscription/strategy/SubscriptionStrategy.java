package com.thomasvallen.appointmentbooking.service.subscription.strategy;

import com.thomasvallen.appointmentbooking.dto.request.NewsletterEmailRequest;
import com.thomasvallen.appointmentbooking.entity.NewsletterSubscriber;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;

public interface SubscriptionStrategy {

    /**
     * Identifies which subscription type this strategy handles
     */
    SubscriptionType getType();

    /**
     * Entry point used by orchestrator / factory
     */
    void sendEmail(NewsletterSubscriber subscriber, NewsletterEmailRequest request);

    /**
     * Builds the email body (HTML / text)
     */
    String buildEmailTemplate(NewsletterEmailRequest request, NewsletterSubscriber subscriber);
}
