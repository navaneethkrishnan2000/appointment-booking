package com.thomasvallen.appointmentbooking.service.subscription.admin;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterEmailRequest;
import com.thomasvallen.appointmentbooking.dto.response.NewsletterSubscriptionResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IAdminNewsletterSubscriptionService {

    ApiResponse<NewsletterSubscriptionResponse> getSubscription(String email);

    ApiResponse<List<NewsletterSubscriptionResponse>> getAllActiveSubscribers();

    CompletableFuture<ApiResponse<Void>> sendNewsletterToSubscribers(NewsletterEmailRequest request);

    CompletableFuture<ApiResponse<Void>> sendNewsletterToSubscriber(String email, NewsletterEmailRequest request);

    ApiResponse<Long> getActiveSubscriberCount();
}
