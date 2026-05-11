package com.topcv.form.dto.response;

import com.topcv.form.enums.FormStatus;

import java.time.LocalDateTime;
import java.util.List;

public record FormResponse(
        Long id,
        String title,
        String description,
        Integer displayOrder,
        FormStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<FieldResponse> fields
) {}