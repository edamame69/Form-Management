package com.topcv.form.validator;

import com.topcv.form.enums.FieldType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FieldValidatorFactory {

    private final TextFieldValidator textValidator;
    private final NumberFieldValidator numberValidator;
    private final DateFieldValidator dateValidator;
    private final ColorFieldValidator colorValidator;
    private final SelectFieldValidator selectValidator;


     //validator tuong ung voi field
    public FieldValidator getValidator(FieldType fieldType) {
        return switch (fieldType) {
            case TEXT -> textValidator;
            case NUMBER -> numberValidator;
            case DATE -> dateValidator;
            case COLOR -> colorValidator;
            case SELECT -> selectValidator;
        };
    }
}