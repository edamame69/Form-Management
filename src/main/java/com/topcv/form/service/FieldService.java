package com.topcv.form.service;

import com.topcv.form.dto.request.FieldRequest;
import com.topcv.form.dto.response.FieldResponse;
import com.topcv.form.entity.Field;
import com.topcv.form.entity.Form;
import com.topcv.form.exception.ResourceNotFoundException;
import com.topcv.form.repository.FieldRepository;
import com.topcv.form.service.mapper.FormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldRepository fieldRepository;
    private final FormService formService;
    private final FormMapper formMapper;

    @Transactional
    public FieldResponse addField(Long formId, FieldRequest request) {
        Form form = formService.findFormOrThrow(formId);

        Field field = Field.builder()
                .form(form)
                .label(request.label())
                .fieldType(request.fieldType())
                .fieldOrder(request.fieldOrder())
                .required(request.required())
                .options(formMapper.serializeOptions(request.options()))
                .minValue(request.minValue())
                .maxValue(request.maxValue())
                .maxLength(request.maxLength())
                .build();

        Field saved = fieldRepository.save(field);
        return formMapper.toFieldResponse(saved);
    }

    @Transactional
    public FieldResponse updateField(Long formId, Long fieldId, FieldRequest request) {
        Field field = findFieldInForm(formId, fieldId);
        field.setLabel(request.label());
        field.setFieldType(request.fieldType());
        field.setFieldOrder(request.fieldOrder());
        field.setRequired(request.required());
        field.setOptions(formMapper.serializeOptions(request.options()));
        field.setMinValue(request.minValue());
        field.setMaxValue(request.maxValue());
        field.setMaxLength(request.maxLength());

        Field updated = fieldRepository.save(field);
        return formMapper.toFieldResponse(updated);
    }

    @Transactional
    public void deleteField(Long formId, Long fieldId) {
        Field field = findFieldInForm(formId, fieldId);
        fieldRepository.delete(field);
    }

    private Field findFieldInForm(Long formId, Long fieldId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> ResourceNotFoundException.of("Field", fieldId));

        if (!field.getForm().getId().equals(formId)) {
            throw new ResourceNotFoundException(
                    String.format("Field %d does not belong to form %d", fieldId, formId)
            );
        }
        return field;
    }
}