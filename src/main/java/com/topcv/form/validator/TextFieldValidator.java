package com.topcv.form.validator;

import com.topcv.form.entity.Field;
import org.springframework.stereotype.Component;

@Component
public class TextFieldValidator implements FieldValidator {

    private static final int DEFAULT_MAX_LENGTH = 200;

    @Override
    public String validate(String value, Field field) {
        // Required check
        if (value == null || value.isBlank()) {
            return field.getRequired() ? "This field is required" : null;
        }

        int maxLength = field.getMaxLength() != null ? field.getMaxLength() : DEFAULT_MAX_LENGTH;
        if (value.length() > maxLength) {
            return String.format("Must not exceed %d characters", maxLength);
        }

        return null;
    }
}