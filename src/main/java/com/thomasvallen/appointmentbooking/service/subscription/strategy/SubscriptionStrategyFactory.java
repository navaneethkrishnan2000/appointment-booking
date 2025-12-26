package com.thomasvallen.appointmentbooking.service.subscription.strategy;

import com.thomasvallen.appointmentbooking.common.exceptions.EmailSenderNotFoundException;
import com.thomasvallen.appointmentbooking.entity.NewsletterSubscriber;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SubscriptionStrategyFactory {

    private final List<SubscriptionStrategy> subscriptionStrategies;
    private Map<SubscriptionType, SubscriptionStrategy> subscriptionStrategyMap;

    public SubscriptionStrategy getSubscriptionStrategy(SubscriptionType type) {
        if (subscriptionStrategyMap == null) {
            subscriptionStrategyMap = subscriptionStrategies.stream()
                    .collect(
                            Collectors.toMap(
                                    SubscriptionStrategy::getType,
                                    Function.identity()
                            )
                    );
        }

        SubscriptionStrategy strategy = subscriptionStrategyMap.get(type);
        if (strategy == null) {
            throw new EmailSenderNotFoundException("No email sender found for type: " + type);
        }
        return strategy;
    }
}
