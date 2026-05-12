package com.topcv.form.controller;

import com.topcv.form.dto.request.SubmitFormRequest;
import com.topcv.form.dto.response.FormResponse;
import com.topcv.form.dto.response.SubmissionResponse;
import com.topcv.form.service.SubmissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Submission", description = "APIs for SW staff to fill and submit forms")
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping("/api/forms/active")
    @Operation(summary = "Get list of active forms sorted by display order")
    public ResponseEntity<List<FormResponse>> getActiveForms() {
        return ResponseEntity.ok(submissionService.getActiveForms());
    }

    @PostMapping("/api/forms/{id}/submit")
    @Operation(summary = "Submit a form with field data")
    public ResponseEntity<SubmissionResponse> submitForm(
            @PathVariable Long id,
            @Valid @RequestBody SubmitFormRequest request) {
        SubmissionResponse response = submissionService.submitForm(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/submissions")
    @Operation(summary = "Get list of all submissions")
    public ResponseEntity<List<SubmissionResponse>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }
}