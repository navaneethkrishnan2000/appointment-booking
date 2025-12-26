package com.thomasvallen.appointmentbooking.service.subscription.strategy;

import com.thomasvallen.appointmentbooking.dto.request.NewsletterEmailRequest;
import com.thomasvallen.appointmentbooking.entity.NewsletterSubscriber;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import org.springframework.stereotype.Component;

@Component
public class NewsletterStrategy implements SubscriptionStrategy {

    @Override
    public SubscriptionType getType() {
        return SubscriptionType.NEWSLETTER;
    }

    @Override
    public void sendEmail(NewsletterSubscriber subscriber, NewsletterEmailRequest request) {

    }

    @Override
    public String buildEmailTemplate(NewsletterEmailRequest request, NewsletterSubscriber subscriber) {
        return null;
    }
}
