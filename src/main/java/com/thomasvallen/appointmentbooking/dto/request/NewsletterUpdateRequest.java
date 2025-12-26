package com.thomasvallen.appointmentbooking.dto.request;

import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterUpdateRequest {

    @NotEmpty(message = "At least one subscription type is required")
    private Set<SubscriptionType> types;
}
