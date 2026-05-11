package com.topcv.form.validator;

import com.topcv.form.entity.Field;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class DateFieldValidator implements FieldValidator {

    @Override
    public String validate(String value, Field field) {
        if (value == null || value.isBlank()) {
            return field.getRequired() ? "This field is required" : null;
        }

        LocalDate date;
        try {
            date = LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            return "Invalid date format (expected yyyy-MM-dd)";
        }

        //Khong cho chon qua khu
        if (date.isBefore(LocalDate.now())) {
            return "Date must not be in the past";
        }

        return null;
    }
}