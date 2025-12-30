package com.thomasvallen.appointmentbooking.controller.event;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.UpdateEventRequest;
import com.thomasvallen.appointmentbooking.dto.response.AddEventRequest;
import com.thomasvallen.appointmentbooking.dto.response.EventResponse;
import com.thomasvallen.appointmentbooking.service.event.IEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/admin/events")
@RequiredArgsConstructor
public class AdminEventController {

    private final IEventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<EventResponse>> addEvent(
            @Valid @RequestBody AddEventRequest request
    ) {
        ApiResponse<EventResponse> response = eventService.addEvent(request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EventResponse>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request
    ) {
        ApiResponse<EventResponse> response = eventService.updateEvent(id, request);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelEvent(
            @PathVariable Long id
    ) {
        ApiResponse<Void> response = eventService.cancelEvent(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEvent(
            @PathVariable Long id
    ) {
        ApiResponse<Void> response = eventService.deleteEvent(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/upcoming")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getUpcomingEvents(

    ) {
        ApiResponse<List<EventResponse>> response = eventService.getUpcomingEvents();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<List<EventResponse>>> getAllEvents(
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String paginationToken,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "next") String direction
    ) {
        ApiResponse<List<EventResponse>> response = eventService
                .getAllEvents(sortBy, paginationToken, pageSize, direction);
        return new ResponseEntity<>(response, response.getStatus());
    }

}
