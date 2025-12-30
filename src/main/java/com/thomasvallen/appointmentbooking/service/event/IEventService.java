package com.thomasvallen.appointmentbooking.service.event;

import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.UpdateEventRequest;
import com.thomasvallen.appointmentbooking.dto.response.AddEventRequest;
import com.thomasvallen.appointmentbooking.dto.response.EventResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IEventService {

    ApiResponse<EventResponse> addEvent(@Valid AddEventRequest request);

    ApiResponse<EventResponse> updateEvent(Long id, @Valid UpdateEventRequest request);

    ApiResponse<Void> cancelEvent(Long id);

    ApiResponse<Void> deleteEvent(Long id);

    ApiResponse<List<EventResponse>> getUpcomingEvents();

    ApiResponse<List<EventResponse>> getAllEvents(String sortBy, String paginationToken, Integer pageSize, String direction);
}
