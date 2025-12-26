package com.thomasvallen.appointmentbooking.controller.newsletter;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterSubscriptionRequest;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterUpdateRequest;
import com.thomasvallen.appointmentbooking.dto.response.NewsletterSubscriptionResponse;
import com.thomasvallen.appointmentbooking.enums.SubscriptionType;
import com.thomasvallen.appointmentbooking.service.subscription.ISubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final ISubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<ApiResponse<NewsletterSubscriptionResponse>> subscribe(
            @Valid @RequestBody NewsletterSubscriptionRequest request
    ) {
        ApiResponse<NewsletterSubscriptionResponse> response = subscriptionService.subscribe(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/subscription/{email}")
    public ResponseEntity<ApiResponse<NewsletterSubscriptionResponse>> updateSubscription(
            @PathVariable String email,
            @Valid @RequestBody NewsletterUpdateRequest request) {
        ApiResponse<NewsletterSubscriptionResponse> response = subscriptionService.updateSubscription(email, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<ApiResponse<Void>> unsubscribeNewsletter(
            @RequestParam String email,
            @RequestParam(required = false) SubscriptionType type
    ) {
        ApiResponse<Void> response = subscriptionService.unsubscribe(email, type);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/resubscribe/{email}")
    public ResponseEntity<ApiResponse<Void>> resubscribe(
            @PathVariable String email,
            @RequestBody Set<SubscriptionType> types
    ) {
        ApiResponse<Void> response = subscriptionService.resubscribe(email, types);
        return new ResponseEntity<>(response, response.getStatus());
    }


}
