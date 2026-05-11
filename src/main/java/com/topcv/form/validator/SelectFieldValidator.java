package com.topcv.form.validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcv.form.entity.Field;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SelectFieldValidator implements FieldValidator {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String validate(String value, Field field) {
        if (value == null || value.isBlank()) {
            return field.getRequired() ? "This field is required" : null;
        }

        List<String> options = parseOptions(field.getOptions());
        if (options.isEmpty()) {
            return "This field has no available options";
        }

        if (!options.contains(value)) {
            return "Value must be one of the available options";
        }

        return null;
    }

    private List<String> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) return Collections.emptyList();
        try {
            return objectMapper.readValue(optionsJson, new TypeReference<>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}