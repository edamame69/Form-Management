package com.topcv.form.controller;

import com.topcv.form.dto.request.FieldRequest;
import com.topcv.form.dto.response.FieldResponse;
import com.topcv.form.service.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms/{formId}/fields")
@RequiredArgsConstructor
@Tag(name = "Field Management", description = "Admin APIs for managing fields within a form")
public class FieldController {

    private final FieldService fieldService;

    @PostMapping
    @Operation(summary = "Add a new field to a form")
    public ResponseEntity<FieldResponse> addField(
            @PathVariable Long formId,
            @Valid @RequestBody FieldRequest request) {
        FieldResponse created = fieldService.addField(formId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{fieldId}")
    @Operation(summary = "Update a field")
    public ResponseEntity<FieldResponse> updateField(
            @PathVariable Long formId,
            @PathVariable Long fieldId,
            @Valid @RequestBody FieldRequest request) {
        return ResponseEntity.ok(fieldService.updateField(formId, fieldId, request));
    }

    @DeleteMapping("/{fieldId}")
    @Operation(summary = "Delete a field from a form")
    public ResponseEntity<Void> deleteField(
            @PathVariable Long formId,
            @PathVariable Long fieldId) {
        fieldService.deleteField(formId, fieldId);
        return ResponseEntity.noContent().build();
    }
}