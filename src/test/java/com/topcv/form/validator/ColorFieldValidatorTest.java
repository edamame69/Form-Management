package com.topcv.form.validator;

import com.topcv.form.entity.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ColorFieldValidatorTest {

    private final ColorFieldValidator validator = new ColorFieldValidator();

    @Test
    void returnsNull_whenValidHex() {
        Field field = Field.builder().required(true).build();
        assertNull(validator.validate("#FF5733", field));
        assertNull(validator.validate("#000000", field));
        assertNull(validator.validate("#abc123", field));
    }

    @Test
    void returnsError_whenInvalidFormat() {
        Field field = Field.builder().required(true).build();
        assertNotNull(validator.validate("FF5733", field));      // thiếu #
        assertNotNull(validator.validate("#FF573", field));      // 5 ký tự
        assertNotNull(validator.validate("#GGGGGG", field));     // ký tự không phải hex
        assertNotNull(validator.validate("red", field));         // tên màu
    }
}