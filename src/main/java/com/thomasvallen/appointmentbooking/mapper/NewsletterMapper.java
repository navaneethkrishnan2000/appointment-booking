package com.thomasvallen.appointmentbooking.mapper;

import com.thomasvallen.appointmentbooking.dto.response.NewsletterSubscriptionResponse;
import com.thomasvallen.appointmentbooking.entity.NewsletterSubscriber;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class NewsletterMapper {

    public NewsletterSubscriptionResponse mapToResponse(@NotNull NewsletterSubscriber subscriber) {
        return NewsletterSubscriptionResponse.builder()
                .id(subscriber.getId())
                .email(subscriber.getEmail())
                .types(subscriber.getTypes())
                .isActive(subscriber.getIsActive())
                .subscribedAt(subscriber.getSubscribedAt())
                .build();
    }
}
