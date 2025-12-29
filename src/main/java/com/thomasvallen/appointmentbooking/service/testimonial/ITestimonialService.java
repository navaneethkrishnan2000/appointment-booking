package com.thomasvallen.appointmentbooking.service.testimonial;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.TestimonialRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateTestimonialRequest;
import com.thomasvallen.appointmentbooking.dto.response.TestimonialResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface ITestimonialService {

    ApiResponse<TestimonialResponse> add(@Valid TestimonialRequest request);

    ApiResponse<List<TestimonialResponse>> getAll(String sortBy, String paginationToken, Integer pageSize, String direction);

    ApiResponse<TestimonialResponse> getById(Long id);

    ApiResponse<TestimonialResponse> update(Long id, UpdateTestimonialRequest request);

    ApiResponse<Void> delete(Long id);
}
