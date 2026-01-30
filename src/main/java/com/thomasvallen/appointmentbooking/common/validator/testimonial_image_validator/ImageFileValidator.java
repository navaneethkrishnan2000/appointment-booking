package com.thomasvallen.appointmentbooking.common.validator.testimonial_image_validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class ImageFileValidator
        implements ConstraintValidator<ValidImageFile, MultipartFile> {

    private long maxSize;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp"
    );

    @Override
    public void initialize(ValidImageFile constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {

        if (file == null || file.isEmpty()) {
            return true;
        }

        if (file.getSize() > maxSize) {
            return false;
        }

        String contentType = file.getContentType();
        return contentType != null && ALLOWED_TYPES.contains(contentType);
    }
}
