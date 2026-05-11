package com.topcv.form.validator;

import com.topcv.form.entity.Field;
import com.topcv.form.entity.Form;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SubmissionValidator {

    private final FieldValidatorFactory validatorFactory;

    public Map<String, String> validate(Form form, Map<String, String> data) {
        Map<String, String> errors = new HashMap<>();

        for (Field field : form.getFields()) {
            String fieldIdKey = String.valueOf(field.getId());
            String value = data.get(fieldIdKey);

            FieldValidator validator = validatorFactory.getValidator(field.getFieldType());
            String error = validator.validate(value, field);

            if (error != null) {
                errors.put(fieldIdKey, error);
            }
        }

        return errors;
    }
}