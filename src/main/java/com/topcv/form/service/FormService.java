package com.topcv.form.service;

import com.topcv.form.dto.request.FormRequest;
import com.topcv.form.dto.response.FormResponse;
import com.topcv.form.entity.Form;
import com.topcv.form.exception.ResourceNotFoundException;
import com.topcv.form.repository.FormRepository;
import com.topcv.form.service.mapper.FormMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;

    @Transactional(readOnly = true)
    public List<FormResponse> getAllForms() {
        return formRepository.findAll().stream()
                .map(form -> formMapper.toFormResponse(form, false))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FormResponse getFormById(Long id) {
        Form form = findFormOrThrow(id);
        return formMapper.toFormResponse(form, true);
    }

    @Transactional
    public FormResponse createForm(FormRequest request) {
        Form form = Form.builder()
                .title(request.title())
                .description(request.description())
                .displayOrder(request.displayOrder())
                .status(request.status())
                .build();
        Form saved = formRepository.save(form);
        return formMapper.toFormResponse(saved, true);
    }

    @Transactional
    public FormResponse updateForm(Long id, FormRequest request) {
        Form form = findFormOrThrow(id);
        form.setTitle(request.title());
        form.setDescription(request.description());
        form.setDisplayOrder(request.displayOrder());
        form.setStatus(request.status());
        Form updated = formRepository.save(form);
        return formMapper.toFormResponse(updated, true);
    }

    @Transactional
    public void deleteForm(Long id) {
        Form form = findFormOrThrow(id);
        formRepository.delete(form);
    }

    public Form findFormOrThrow(Long id) {
        return formRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Form", id));
    }
}