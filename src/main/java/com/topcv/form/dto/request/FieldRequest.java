package com.topcv.form.dto.request;

import com.topcv.form.enums.FieldType;
import jakarta.validation.constraints.*;

import java.util.List;

public record FieldRequest(
        @NotBlank(message = "Label is required")
        @Size(max = 255, message = "Label must not exceed 255 characters")
        String label,

        @NotNull(message = "Field type is required")
        FieldType fieldType,

        @NotNull(message = "Field order is required")
        @Min(value = 0, message = "Field order must be >= 0")
        Integer fieldOrder,

        @NotNull(message = "Required flag must be specified")
        Boolean required,

        List<String> options,
        Double minValue,
        Double maxValue,

        @Min(value = 1, message = "Max length must be >= 1")
        Integer maxLength
) {}