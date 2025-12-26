package com.thomasvallen.appointmentbooking.service.subscription;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterSubscriptionRequest;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterUpdateRequest;
import com.thomasvallen.appointmentbooking.dto.response.NewsletterSubscriptionResponse;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import jakarta.validation.Valid;

import java.util.Set;

public interface ISubscriptionService {
    ApiResponse<NewsletterSubscriptionResponse> subscribe(@Valid NewsletterSubscriptionRequest email);

    ApiResponse<Void> unsubscribe(String email, SubscriptionType type);

    ApiResponse<NewsletterSubscriptionResponse> updateSubscription(String email, NewsletterUpdateRequest request);

    ApiResponse<Void> resubscribe(String email, Set<SubscriptionType> types);
}
