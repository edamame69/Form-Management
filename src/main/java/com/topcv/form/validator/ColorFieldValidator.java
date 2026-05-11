package com.topcv.form.validator;

import com.topcv.form.entity.Field;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ColorFieldValidator implements FieldValidator {

    private static final Pattern HEX_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    @Override
    public String validate(String value, Field field) {
        if (value == null || value.isBlank()) {
            return field.getRequired() ? "This field is required" : null;
        }

        if (!HEX_PATTERN.matcher(value).matches()) {
            return "Must be a valid HEX color (e.g. #FF5733)";
        }

        return null;
    }
}