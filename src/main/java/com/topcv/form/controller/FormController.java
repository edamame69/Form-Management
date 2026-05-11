package com.topcv.form.controller;

import com.topcv.form.dto.request.FormRequest;
import com.topcv.form.dto.response.FormResponse;
import com.topcv.form.service.FormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@Tag(name = "Form Management", description = "Admin APIs for managing forms")
public class FormController {

    private final FormService formService;

    @GetMapping
    @Operation(summary = "Get all forms")
    public ResponseEntity<List<FormResponse>> getAllForms() {
        return ResponseEntity.ok(formService.getAllForms());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get form detail with all fields")
    public ResponseEntity<FormResponse> getFormById(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFormById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new form")
    public ResponseEntity<FormResponse> createForm(@Valid @RequestBody FormRequest request) {
        FormResponse created = formService.createForm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update form information")
    public ResponseEntity<FormResponse> updateForm(
            @PathVariable Long id,
            @Valid @RequestBody FormRequest request) {
        return ResponseEntity.ok(formService.updateForm(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a form (cascade deletes fields)")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }
}