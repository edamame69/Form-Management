package com.topcv.form.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcv.form.dto.request.SubmitFormRequest;
import com.topcv.form.dto.response.FormResponse;
import com.topcv.form.dto.response.SubmissionResponse;
import com.topcv.form.entity.Form;
import com.topcv.form.entity.Submission;
import com.topcv.form.enums.FormStatus;
import com.topcv.form.exception.SubmissionValidationException;
import com.topcv.form.repository.FormRepository;
import com.topcv.form.repository.SubmissionRepository;
import com.topcv.form.service.mapper.FormMapper;
import com.topcv.form.validator.SubmissionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final FormRepository formRepository;
    private final SubmissionRepository submissionRepository;
    private final FormService formService;
    private final FormMapper formMapper;
    private final SubmissionValidator submissionValidator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Lấy danh sách form active, sắp xếp theo display_order.
     * Không kèm fields để response gọn (nhân viên chỉ xem danh sách).
     */
    @Transactional(readOnly = true)
    public List<FormResponse> getActiveForms() {
        return formRepository.findByStatusOrderByDisplayOrderAsc(FormStatus.ACTIVE)
                .stream()
                .map(form -> formMapper.toFormResponse(form, true))
                .collect(Collectors.toList());
    }

    @Transactional
    public SubmissionResponse submitForm(Long formId, SubmitFormRequest request) {
        Form form = formService.findFormOrThrow(formId);

        // Không cho submit form DRAFT
        if (form.getStatus() != FormStatus.ACTIVE) {
            throw new SubmissionValidationException(
                    Map.of("_form", "Form is not active and cannot accept submissions")
            );
        }

        Map<String, String> errors = submissionValidator.validate(form, request.data());
        if (!errors.isEmpty()) {
            throw new SubmissionValidationException(errors);
        }

        // Lưu data dưới dạng JSON string
        String dataJson = serializeData(request.data());

        Submission submission = Submission.builder()
                .form(form)
                .submissionData(dataJson)
                .build();

        Submission saved = submissionRepository.save(submission);

        return new SubmissionResponse(
                saved.getId(),
                form.getId(),
                form.getTitle(),
                request.data(),
                saved.getSubmittedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<SubmissionResponse> getAllSubmissions() {
        return submissionRepository.findAllByOrderBySubmittedAtDesc()
                .stream()
                .map(this::toSubmissionResponse)
                .collect(Collectors.toList());
    }

    private SubmissionResponse toSubmissionResponse(Submission submission) {
        return new SubmissionResponse(
                submission.getId(),
                submission.getForm().getId(),
                submission.getForm().getTitle(),
                parseData(submission.getSubmissionData()),
                submission.getSubmittedAt()
        );
    }

    private String serializeData(Map<String, String> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize submission data", e);
        }
    }

    private Map<String, String> parseData(String json) {
        if (json == null || json.isBlank()) return Collections.emptyMap();
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyMap();
        }
    }
}