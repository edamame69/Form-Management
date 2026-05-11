package com.topcv.form.dto.request;

import com.topcv.form.enums.FormStatus;
import jakarta.validation.constraints.*;

public record FormRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @NotNull(message = "Display order is required")
        @Min(value = 0, message = "Display order must be >= 0")
        Integer displayOrder,

        @NotNull(message = "Status is required")
        FormStatus status
) {}