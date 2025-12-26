package com.thomasvallen.appointmentbooking.dto.request;

import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsletterEmailRequest {

    @NotNull(message = "Subscription type is required")
    private SubscriptionType type;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Content is required")
    private String content;

    private Map<String, Object> templateData;
}
