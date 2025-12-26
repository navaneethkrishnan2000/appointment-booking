package com.thomasvallen.appointmentbooking.service.subscription.admin;

import com.thomasvallen.appointmentbooking.common.exceptions.NewsletterSubscriberNotFoundException;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterEmailRequest;
import com.thomasvallen.appointmentbooking.dto.response.NewsletterSubscriptionResponse;
import com.thomasvallen.appointmentbooking.entity.NewsletterSubscriber;
import com.thomasvallen.appointmentbooking.mapper.NewsletterMapper;
import com.thomasvallen.appointmentbooking.repository.SubscriptionRepository;
import com.thomasvallen.appointmentbooking.service.subscription.strategy.SubscriptionStrategy;
import com.thomasvallen.appointmentbooking.service.subscription.strategy.SubscriptionStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminNewsletterSubscriptionService implements IAdminNewsletterSubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionStrategyFactory strategyFactory;
    private final NewsletterMapper newsletterMapper;

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<NewsletterSubscriptionResponse> getSubscription(String email) {
        NewsletterSubscriber subscriber = subscriptionRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new NewsletterSubscriberNotFoundException("Subscriber not found: " + email));

        return ApiResponse.success(
                newsletterMapper.mapToResponse(subscriber),
                "Subscriber fetched successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<NewsletterSubscriptionResponse>> getAllActiveSubscribers() {
        List<NewsletterSubscriptionResponse> responses =
                subscriptionRepository.findByIsActiveTrue()
                        .stream()
                        .map(newsletterMapper::mapToResponse)
                        .toList();

        return ApiResponse.success(responses, null);
    }

    @Override
    @Async
    public CompletableFuture<ApiResponse<Void>> sendNewsletterToSubscribers(NewsletterEmailRequest request) {
        log.info("Sending newsletter of type: {} to all subscribers", request.getType());

        List<NewsletterSubscriber> subscribers = subscriptionRepository
                .findActiveSubscribersByType(request.getType());

        SubscriptionStrategy subscriptionStrategy = strategyFactory.getSubscriptionStrategy(request.getType());

        subscribers.forEach(subscriber -> {
            try {
                subscriptionStrategy.sendEmail(subscriber, request);
            } catch (Exception e) {
                log.error(
                        "Failed to send newsletter to: {}",
                        subscriber.getEmail(),
                        e
                );
            }
        });

        log.info("Newsletter sent to {} subscribers", subscribers.size());

        return CompletableFuture.completedFuture(
                ApiResponse.success("Newsletter sent successfully")
        );
    }

    @Override
    @Async
    public CompletableFuture<ApiResponse<Void>> sendNewsletterToSubscriber(String email, NewsletterEmailRequest request) {
        log.info("Sending newsletter of type: {} to: {}", request.getType(), email);

        NewsletterSubscriber subscriber = subscriptionRepository
                .findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new NewsletterSubscriberNotFoundException("Subscriber not found: " + email));

        if (!subscriber.getIsActive() || !subscriber.getTypes().contains(request.getType())) {
            throw new IllegalStateException("Subscriber is not active or not subscribed to this type");
        }

        SubscriptionStrategy subscriptionStrategy = strategyFactory.getSubscriptionStrategy(request.getType());
        subscriptionStrategy.sendEmail(subscriber, request);

        log.info("Newsletter sent successfully to: {}", email);

        return CompletableFuture.completedFuture(
                ApiResponse.success("Newsletter sent successfully")
        );
    }

    @Override
    public ApiResponse<Long> getActiveSubscriberCount() {
        long count = subscriptionRepository.countActiveSubscribers();
        return ApiResponse.success(count);
    }

    @Async
    private void sendConfirmationEmail(NewsletterSubscriber subscriber) {
        // Implementation for sending confirmation email
        log.info("Sending confirmation email to: {}", subscriber.getEmail());
        // Add actual email sending logic here
    }

}
