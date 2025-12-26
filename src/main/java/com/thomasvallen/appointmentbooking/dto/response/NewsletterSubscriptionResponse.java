package com.thomasvallen.appointmentbooking.dto.response;

import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsletterSubscriptionResponse {

    private Long id;
    private String email;
    private Set<SubscriptionType> types;
    private Boolean isActive;
    private Instant subscribedAt;
}
