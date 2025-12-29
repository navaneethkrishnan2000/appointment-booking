package com.thomasvallen.appointmentbooking.service.testimonial;

import com.thomasvallen.appointmentbooking.common.exceptions.ResourceNotFoundException;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.TestimonialRequest;
import com.thomasvallen.appointmentbooking.dto.request.UpdateTestimonialRequest;
import com.thomasvallen.appointmentbooking.dto.response.TestimonialResponse;
import com.thomasvallen.appointmentbooking.entity.Testimonial;
import com.thomasvallen.appointmentbooking.enums.TestimonialStatus;
import com.thomasvallen.appointmentbooking.mapper.TestimonialMapper;
import com.thomasvallen.appointmentbooking.repository.TestimonialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestimonialService implements ITestimonialService {

    private final TestimonialRepository testimonialRepository;
    private final TestimonialMapper mapper;

    @Override
    public ApiResponse<TestimonialResponse> add(
            @NotNull TestimonialRequest request
    ) {
        Testimonial testimonial = Testimonial.builder()
                .name(request.getName())
                .designation(request.getDesignation())
                .message(request.getMessage())
                .status(TestimonialStatus.APPROVED)
                .build();

        MultipartFile image = request.getProfileImage();
        if (image != null && !image.isEmpty()) {
            try {
                testimonial.setProfileImage(image.getBytes());
                testimonial.setProfileImageContentType(image.getContentType());
                testimonial.setProfileImageName(image.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile image", e);
            }
        }

        testimonial = testimonialRepository.save(testimonial);

        return ApiResponse.success(
                mapper.mapToTestimonialResponse(testimonial),
                "Testimonial Added Successfully"
        );
    }

    @Override
    public ApiResponse<List<TestimonialResponse>> getAll(
            String sortBy,
            String paginationToken,
            Integer pageSize,
            String direction
    ) {
        Map<String, Object> metadata = new HashMap<>();

        int limit = (pageSize != null && pageSize > 0) ? pageSize : 10;
        int offset = 0;

        if (paginationToken != null && !paginationToken.isEmpty()) {
            // Decode the token which now contains both current and previous offsets
            String decodedToken = new String(Base64.getDecoder().decode(paginationToken));
            String[] offsetValues = decodedToken.split(":");

            if ("prev".equals(direction) && offsetValues.length > 1) {
                // If going backward, use the previous offset
                offset = Integer.parseInt(offsetValues[1]);
            } else {
                // If going forward or direction not specified, use the current offset
                offset = Integer.parseInt(offsetValues[0]);
            }
        }

        int pageNumber = offset / limit;

        Sort sort = "asc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, limit, sort);

        Page<Testimonial> testimonialPage = testimonialRepository.findAll(pageable);

        if (testimonialPage.isEmpty()) {
            return new ApiResponse<>(
                    Collections.emptyList(),
                    false,
                    "No testimonials found",
                    Instant.now(),
                    HttpStatus.NO_CONTENT,
                    metadata
            );
        }

        // Generate next pagination token
        int nextOffset = offset + limit;

        // Create a token that contains both current and previous offsets
        // Format: currentOffset:previousOffset
        String nextPaginationToken = Base64.getEncoder().encodeToString(
                (nextOffset + ":" + offset).getBytes()
        );

        // Generate previous pagination token (if we're not on the first page)
        String prevPaginationToken = null;
        if (offset >= limit) {
            int prevOffset = Math.max(0, offset - limit);
            prevPaginationToken = Base64.getEncoder().encodeToString(
                    (offset + ":" +prevOffset).getBytes()
            );
        }

        metadata.put("nextPaginationToken", nextPaginationToken);
        metadata.put("prevPaginationToken", prevPaginationToken);
        metadata.put("hasMore", testimonialPage.hasNext());
        metadata.put("hasPrevious", offset > 0);

        metadata.put("currentPage", testimonialPage.getNumber());
        metadata.put("pageSize", testimonialPage.getSize());
        metadata.put("totalItems", testimonialPage.getTotalElements());
        metadata.put("totalPages", testimonialPage.getTotalPages());

        List<TestimonialResponse> testimonialResponses =
                testimonialPage.getContent().stream()
                        .map(mapper::mapToTestimonialResponse)
                        .toList();

        return ApiResponse.success(
                testimonialResponses,
                "Testimonials fetched successfully"
        );
    }

    @Override
    public ApiResponse<TestimonialResponse> getById(
            Long id
    ) {
        Testimonial testimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));

        return ApiResponse.success(
          mapper.mapToTestimonialResponse(testimonial),
          "Testimonial Fetched Successfully"
        );
    }

    @Override
    public ApiResponse<TestimonialResponse> update(
            Long id,
            @NotNull UpdateTestimonialRequest request
    ) {
        Testimonial testimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));

        testimonial.setName(request.getName());
        testimonial.setDesignation(request.getDesignation());
        testimonial.setMessage(request.getMessage());
        testimonial.setStatus(request.getStatus());

        MultipartFile image = request.getProfileImage();
        if (image != null && !image.isEmpty()) {
            try {
                testimonial.setProfileImage(image.getBytes());
                testimonial.setProfileImageContentType(image.getContentType());
                testimonial.setProfileImageName(image.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read image file", e);
            }
        }

        testimonial = testimonialRepository.save(testimonial);

        return ApiResponse.success(
                mapper.mapToTestimonialResponse(testimonial),
                "Testimonial Updated Successfully"
        );
    }

    @Override
    public ApiResponse<Void> delete(
            Long id
    ) {
        Testimonial testimonial = testimonialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Testimonial not found"));

        testimonialRepository.delete(testimonial);

        return ApiResponse.success(
                "Testimonial Deleted Successfully"
        );
    }
}
