package com.topcv.form.validator;

import com.topcv.form.entity.Field;

public interface FieldValidator {
    String validate(String value, Field field);
}