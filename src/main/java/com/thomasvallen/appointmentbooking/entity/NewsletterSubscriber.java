package com.thomasvallen.appointmentbooking.entity;

import com.thomasvallen.appointmentbooking.common.BaseEntity;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "newsletter_subscribers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsletterSubscriber extends BaseEntity {

    @Column(unique = true)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<SubscriptionType> types;

    private Boolean isActive;
    private Instant subscribedAt;
    private Instant unsubscribedAt;
}
