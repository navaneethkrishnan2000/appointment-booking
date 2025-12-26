package com.thomasvallen.appointmentbooking.controller.newsletter;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.NewsletterEmailRequest;
import com.thomasvallen.appointmentbooking.dto.response.NewsletterSubscriptionResponse;
import com.thomasvallen.appointmentbooking.service.subscription.admin.IAdminNewsletterSubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/admin/newsletter")
@RequiredArgsConstructor
public class AdminNewsletterController {

    private final IAdminNewsletterSubscriptionService adminNewsletterSubscriptionService;

    @GetMapping("/subscription/{email}")
    public ResponseEntity<ApiResponse<NewsletterSubscriptionResponse>>
    getSubscription(
            @PathVariable String email
    ) {
        ApiResponse<NewsletterSubscriptionResponse> response =
                adminNewsletterSubscriptionService.getSubscription(email);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/subscribers/active")
    public ResponseEntity<ApiResponse<List<NewsletterSubscriptionResponse>>>
    getAllActiveSubscribers(

    ) {
        ApiResponse<List<NewsletterSubscriptionResponse>> response =
                adminNewsletterSubscriptionService.getAllActiveSubscribers();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/send")
    public ResponseEntity<CompletableFuture<ApiResponse<Void>>> sendNewsletterToSubscribers(
            @Valid @RequestBody NewsletterEmailRequest request
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<ApiResponse<Void>> response = adminNewsletterSubscriptionService.sendNewsletterToSubscribers(request);
        return new ResponseEntity<>(response, response.get().getStatus());
    }

    @PostMapping("/send/{email}")
    public ResponseEntity<CompletableFuture<ApiResponse<Void>>> sendNewsletterToSubscriber(
            @PathVariable String email,
            @Valid @RequestBody NewsletterEmailRequest request
    ) throws ExecutionException, InterruptedException {
        CompletableFuture<ApiResponse<Void>> response = adminNewsletterSubscriptionService.sendNewsletterToSubscriber(email, request);
        return new ResponseEntity<>(response, response.get().getStatus());
    }

    @GetMapping("/subscribers/count")
    public ResponseEntity<ApiResponse<Long>> getActiveSubscriberCount(

    ) {
        ApiResponse<Long> response = adminNewsletterSubscriptionService.getActiveSubscriberCount();
        return new ResponseEntity<>(response, response.getStatus());
    }
}
