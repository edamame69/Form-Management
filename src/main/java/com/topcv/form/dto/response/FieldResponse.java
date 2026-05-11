package com.topcv.form.dto.response;

import com.topcv.form.enums.FieldType;

import java.util.List;

public record FieldResponse(
        Long id,
        String label,
        FieldType fieldType,
        Integer fieldOrder,
        Boolean required,
        List<String> options,
        Double minValue,
        Double maxValue,
        Integer maxLength
) {}