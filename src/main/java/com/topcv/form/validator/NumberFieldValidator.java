package com.topcv.form.validator;

import com.topcv.form.entity.Field;
import org.springframework.stereotype.Component;

@Component
public class NumberFieldValidator implements FieldValidator {

    @Override
    public String validate(String value, Field field) {
        if (value == null || value.isBlank()) {
            return field.getRequired() ? "This field is required" : null;
        }

        double number;
        try {
            number = Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return "Must be a valid number";
        }

        if (field.getMinValue() != null && number < field.getMinValue()) {
            return String.format("Must be >= %s", field.getMinValue());
        }
        if (field.getMaxValue() != null && number > field.getMaxValue()) {
            return String.format("Must be <= %s", field.getMaxValue());
        }

        return null;
    }
}