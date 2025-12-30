package com.thomasvallen.appointmentbooking.service.event;

import com.thomasvallen.appointmentbooking.common.exceptions.ResourceNotFoundException;
import com.thomasvallen.appointmentbooking.common.utils.ApiResponse;
import com.thomasvallen.appointmentbooking.dto.request.UpdateEventRequest;
import com.thomasvallen.appointmentbooking.dto.response.AddEventRequest;
import com.thomasvallen.appointmentbooking.dto.response.EventResponse;
import com.thomasvallen.appointmentbooking.entity.Event;
import com.thomasvallen.appointmentbooking.enums.EventStatus;
import com.thomasvallen.appointmentbooking.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private final EventRepository eventRepository;


    @Override
    public ApiResponse<EventResponse> addEvent(AddEventRequest request) {

        Instant now = Instant.now();

        if (request.getEventDateTime().isBefore(now) ||
                request.getEventDateTime().equals(now)) {

            return ApiResponse.badRequest(
                    "Event date & time must be in the future"
            );
        }

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .zoomLink(request.getZoomLink())
                .eventDateTime(request.getEventDateTime())
                .status(EventStatus.UPCOMING)
                .build();

        event = eventRepository.save(event);

        return ApiResponse.created(
                mapToResponse(event),
                "Event created successfully"
        );
    }

    @Override
    public ApiResponse<EventResponse> updateEvent(Long id, UpdateEventRequest request) {

        Event event = getEventById(id);

        if (event.getStatus() == EventStatus.CANCELLED) {
            return ApiResponse.badRequest(
                    "Cancelled events cannot be updated"
            );
        }

        if (event.getStatus() == EventStatus.COMPLETED) {
            return ApiResponse.badRequest(
                    "Completed events cannot be updated"
            );
        }

        if (request.getTitle() != null)
            event.setTitle(request.getTitle());

        if (request.getDescription() != null)
            event.setDescription(request.getDescription());

        if (request.getZoomLink() != null)
            event.setZoomLink(request.getZoomLink());

        if (request.getEventDateTime() != null) {
            if (!request.getEventDateTime().isAfter(Instant.now())) {
                return ApiResponse.badRequest(
                        "Updated event date & time must be in the future"
                );
            }
            event.setEventDateTime(request.getEventDateTime());
        }

        event = eventRepository.save(event);

        return ApiResponse.success(
                mapToResponse(event),
                "Event updated successfully"
        );
    }

    @Override
    public ApiResponse<Void> cancelEvent(Long id) {

        Event event = getEventById(id);

        if (event.getStatus() == EventStatus.CANCELLED) {
            return ApiResponse.badRequest(
                    "Event is already cancelled"
            );
        }

        if (event.getStatus() == EventStatus.COMPLETED) {
            return ApiResponse.badRequest(
                    "Completed events cannot be cancelled"
            );
        }

        event.setStatus(EventStatus.CANCELLED);
        event.setUpdatedAt(Instant.now());

        eventRepository.save(event);

        return ApiResponse.success(
                "Event cancelled successfully"
        );
    }

    @Override
    public ApiResponse<Void> deleteEvent(Long id) {

        Event event = getEventById(id);

        eventRepository.delete(event);

        return ApiResponse.success(
                "Event deleted successfully"
        );
    }

    @Override
    public ApiResponse<List<EventResponse>> getUpcomingEvents() {
        
        Instant now = Instant.now();
        
        List<EventResponse> responses = eventRepository
                .findByStatusOrderByEventDateTimeAsc(EventStatus.UPCOMING)
                .stream()
                .filter(event -> event.getEventDateTime().isAfter(now))
                .map(this::mapToResponse)
                .toList();
        
        return ApiResponse.success(
                responses,
                "Upcoming events fetched successfully"
        );
    }

    @Override
    public ApiResponse<List<EventResponse>> getAllEvents(
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

        Page<Event> eventPage = eventRepository.findAll(pageable);
        
        if (eventPage.isEmpty()) {
            return new ApiResponse<>(
                    Collections.emptyList(),
                    false,
                    "No events found",
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
        metadata.put("hasMore", eventPage.hasNext());
        metadata.put("hasPrevious", offset > 0);

        metadata.put("currentPage", eventPage.getNumber());
        metadata.put("pageSize", eventPage.getSize());
        metadata.put("totalItems", eventPage.getTotalElements());
        metadata.put("totalPages", eventPage.getTotalPages());

        List<EventResponse> eventResponses = eventPage
                .getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return ApiResponse.success(
                eventResponses,
                "Events fetched successfully",
                metadata
        );
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .zoomLink(event.getZoomLink())
                .status(event.getStatus())
                .build();
    }

    private Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
    }
}
