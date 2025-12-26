package com.thomasvallen.appointmentbooking.service.subscription;

import com.thomasvallen.appointmentbooking.common.exceptions.DuplicateSubscriptionException;
import com.thomasvallen.appointmentbooking.common.exceptions.NewsletterSubscriberNotFoundException;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterSubscriptionRequest;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterUpdateRequest;
import com.thomasvallen.appointmentbooking.dto.response.NewsletterSubscriptionResponse;
import com.thomasvallen.appointmentbooking.entity.NewsletterSubscriber;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import com.thomasvallen.appointmentbooking.mapper.NewsletterMapper;
import com.thomasvallen.appointmentbooking.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final NewsletterMapper newsletterMapper;

    @Override
    @Transactional
    public ApiResponse<NewsletterSubscriptionResponse> subscribe(
            @NotNull @Valid NewsletterSubscriptionRequest request
    ) {
        log.info("Processing subscription request for email: {}", request.getEmail());

        if (subscriptionRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateSubscriptionException("Email already subscribed: " + request.getEmail());
        }

        NewsletterSubscriber subscriber = NewsletterSubscriber.builder()
                .email(request.getEmail())
                .types(new HashSet<>(request.getTypes()))
                .isActive(true)
                .subscribedAt(Instant.now())
                .build();

        subscriber = subscriptionRepository.save(subscriber);
        log.info("Subscription created successfully for: {}", request.getEmail());

        // TODO : send confirmation email asynchronously
        return ApiResponse.created(newsletterMapper.mapToResponse(subscriber), "Successfully subscribed to newsletter") ;
    }

    @Override
    public ApiResponse<Void> unsubscribe(String email, SubscriptionType type) {
        log.info("Processing unsubscribe request for email: {} and type: {}", email, type);

        NewsletterSubscriber subscriber = subscriptionRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new NewsletterSubscriberNotFoundException("Subscriber not found : " + email));

        if (type == null) {
            // Unsubscribe from all
            subscriber.setIsActive(false);
            subscriber.setUnsubscribedAt(Instant.now());
            subscriber.getTypes().clear();
            log.info("Unsubscribed {} from all newsletters", email);
        } else {
            // Unsubscribe from specific type
            subscriber.getTypes().remove(type);
            if (subscriber.getTypes().isEmpty()) {
                subscriber.setIsActive(false);
                subscriber.setUnsubscribedAt(Instant.now());
            }
            log.info("Unsubscribed {} from {}", email, type);
        }

        subscriptionRepository.save(subscriber);

        return ApiResponse.success(
                type == null
                        ? "Successfully unsubscribed from all newsletters"
                        : "Successfully unsubscribed from " + type
        );
    }

    @Override
    public ApiResponse<NewsletterSubscriptionResponse> updateSubscription(String email, @NotNull NewsletterUpdateRequest request) {
        log.info("Updating subscription for email: {}", email);

        NewsletterSubscriber subscriber = subscriptionRepository.findByEmail(email)
                .orElseThrow(() -> new NewsletterSubscriberNotFoundException("Subscriber not found: " + email));

        subscriber.setTypes(new HashSet<>(request.getTypes()));
        subscriber = subscriptionRepository.save(subscriber);

        log.info("Subscription updated successfully for: {}", email);
        return ApiResponse.success(newsletterMapper.mapToResponse(subscriber), "Subscription preferences updated successfully");
    }

    @Override
    public ApiResponse<Void> resubscribe(String email, Set<SubscriptionType> types) {
        log.info("Processing resubscribe request for email: {}", email);

        if (types == null || types.isEmpty()) {
            throw new IllegalArgumentException("At least one subscription type must be provided");
        }


        NewsletterSubscriber subscriber = subscriptionRepository.findByEmail(email)
                .orElseThrow(() -> new NewsletterSubscriberNotFoundException("Subscriber not found: " + email));

        subscriber.setIsActive(true);
        subscriber.setTypes(new HashSet<>(types));
        subscriber.setUnsubscribedAt(null);
        subscriber.setSubscribedAt(Instant.now());

        subscriptionRepository.save(subscriber);

        log.info("Resubscribed {} successfully", email);

        return ApiResponse.success("Successfully resubscribed to selected newsletters");
    }


}
