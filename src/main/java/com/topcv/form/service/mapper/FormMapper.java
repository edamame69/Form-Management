package com.topcv.form.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcv.form.dto.response.FieldResponse;
import com.topcv.form.dto.response.FormResponse;
import com.topcv.form.entity.Field;
import com.topcv.form.entity.Form;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FormMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public FormResponse toFormResponse(Form form, boolean includeFields) {
        List<FieldResponse> fieldResponses = includeFields
                ? form.getFields().stream().map(this::toFieldResponse).collect(Collectors.toList())
                : null;

        return new FormResponse(
                form.getId(),
                form.getTitle(),
                form.getDescription(),
                form.getDisplayOrder(),
                form.getStatus(),
                form.getCreatedAt(),
                form.getUpdatedAt(),
                fieldResponses
        );
    }

    public FieldResponse toFieldResponse(Field field) {
        return new FieldResponse(
                field.getId(),
                field.getLabel(),
                field.getFieldType(),
                field.getFieldOrder(),
                field.getRequired(),
                parseOptions(field.getOptions()),
                field.getMinValue(),
                field.getMaxValue(),
                field.getMaxLength()
        );
    }

    public String serializeOptions(List<String> options) {
        if (options == null || options.isEmpty()) return null;
        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize options", e);
        }
    }

    public List<String> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(optionsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }
}