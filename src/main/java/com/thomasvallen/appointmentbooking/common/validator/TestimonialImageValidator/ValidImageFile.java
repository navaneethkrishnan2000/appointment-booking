package com.thomasvallen.appointmentbooking.common.validator.TestimonialImageValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageFileValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageFile {

    String message() default "Invalid image file";

    long maxSize() default 2 * 1024 * 1024;

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
