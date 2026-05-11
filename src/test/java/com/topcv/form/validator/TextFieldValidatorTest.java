package com.topcv.form.validator;

import com.topcv.form.entity.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextFieldValidatorTest {

    private final TextFieldValidator validator = new TextFieldValidator();

    @Test
    void returnsNull_whenValueIsValid() {
        Field field = Field.builder().required(true).maxLength(200).build();
        assertNull(validator.validate("Hello", field));
    }

    @Test
    void returnsError_whenRequiredAndBlank() {
        Field field = Field.builder().required(true).maxLength(200).build();
        assertEquals("This field is required", validator.validate("", field));
        assertEquals("This field is required", validator.validate(null, field));
    }

    @Test
    void returnsNull_whenNotRequiredAndBlank() {
        Field field = Field.builder().required(false).maxLength(200).build();
        assertNull(validator.validate("", field));
        assertNull(validator.validate(null, field));
    }

    @Test
    void returnsError_whenExceedsMaxLength() {
        Field field = Field.builder().required(true).maxLength(5).build();
        String result = validator.validate("Hello World", field);
        assertNotNull(result);
        assertTrue(result.contains("5"));
    }
}