package com.thomasvallen.appointmentbooking.controller.testimonial;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.TestimonialRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateTestimonialRequest;
import com.thomasvallen.appointmentbooking.dto.response.TestimonialResponse;
import com.thomasvallen.appointmentbooking.service.testimonial.ITestimonialService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/admin/testimonials")
@RequiredArgsConstructor
public class AdminTestimonialController {

    private final ITestimonialService testimonialService;

    @PostMapping(
            value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<TestimonialResponse>> add(
            @Valid @ModelAttribute TestimonialRequest request
    ) {
        ApiResponse<TestimonialResponse> response = testimonialService.add(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<List<TestimonialResponse>>> getAll(
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String paginationToken,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "next") String direction
    ) {
        ApiResponse<List<TestimonialResponse>> responses =
                testimonialService.getAll(sortBy, paginationToken, pageSize, direction);
        return new ResponseEntity<>(responses, responses.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestimonialResponse>> getById(
            @PathVariable Long id
    ) {
        ApiResponse<TestimonialResponse> response = testimonialService.getById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping(
            value = "/update/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ApiResponse<TestimonialResponse>> update(
            @PathVariable Long id,
            @ModelAttribute @Valid UpdateTestimonialRequest request
    ) {
        ApiResponse<TestimonialResponse> response = testimonialService.update(id, request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id
    ) {
        ApiResponse<Void> response = testimonialService.delete(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
