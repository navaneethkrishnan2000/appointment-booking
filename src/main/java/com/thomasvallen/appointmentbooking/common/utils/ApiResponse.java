package com.thomasvallen.appointmentbooking.common.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private T data;
    private boolean success;
    private String message;
    private Instant timestamp;
    private HttpStatus status;
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Map<String, Object> metadata = new HashMap<>();

    public static <T> ApiResponse<T> success(T data, String message, Map<String, Object> metadata ) {
        return ApiResponse.<T>builder()
                .data(data)
                .metadata(metadata)
                .success(true)
                .message(message)
                .status(HttpStatus.OK)
                .timestamp(Instant.now())
                .build();
    }

    // Static factory methods
    public static <T> ApiResponse<T> success(T data, String message ) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(true)
                .message(message)
                .status(HttpStatus.OK)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message ) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(true)
                .message(message)
                .status(HttpStatus.OK)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> created(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .success(false)
                .message(message)
                .status(HttpStatus.FORBIDDEN)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> unauthorized(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .status(HttpStatus.UNAUTHORIZED)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .status(HttpStatus.NOT_FOUND)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> badRequest(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> conflict(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .status(HttpStatus.CONFLICT)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> forbidden(String message) {
        return ApiResponse.<T>builder()
                .data(null)
                .success(false)
                .message(message)
                .status(HttpStatus.FORBIDDEN)
                .timestamp(Instant.now())
                .build();
    }
}
