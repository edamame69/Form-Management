package com.topcv.form.controller;

import com.topcv.form.dto.request.FieldRequest;
import com.topcv.form.dto.response.ErrorResponse;
import com.topcv.form.dto.response.FieldResponse;
import com.topcv.form.service.FieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/forms/{formId}/fields")
@RequiredArgsConstructor
@Tag(name = "2. Field Management", description = "Admin APIs - Quản lý field bên trong form")
public class FieldController {

    private final FieldService fieldService;

    @PostMapping
    @Operation(
            summary = "Thêm field mới vào form",
            description = """
                    Tạo field với label, fieldType (TEXT/NUMBER/DATE/COLOR/SELECT), thứ tự, required.
                    
                    Tùy theo fieldType, các trường validation khác nhau được áp dụng:
                    - TEXT: maxLength (default 200)
                    - NUMBER: minValue, maxValue
                    - DATE: không cho phép chọn ngày quá khứ
                    - COLOR: phải là HEX hợp lệ (#RRGGBB)
                    - SELECT: options bắt buộc (List<String>)
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Thêm field thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Form không tồn tại")
    })
    public ResponseEntity<FieldResponse> addField(
            @PathVariable Long formId,
            @Valid @RequestBody FieldRequest request) {
        FieldResponse created = fieldService.addField(formId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{fieldId}")
    @Operation(summary = "Cập nhật field")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "404", description = "Form hoặc field không tồn tại")
    })
    public ResponseEntity<FieldResponse> updateField(
            @PathVariable Long formId,
            @PathVariable Long fieldId,
            @Valid @RequestBody FieldRequest request) {
        return ResponseEntity.ok(fieldService.updateField(formId, fieldId, request));
    }

    @DeleteMapping("/{fieldId}")
    @Operation(summary = "Xóa field khỏi form")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Form hoặc field không tồn tại")
    })
    public ResponseEntity<Void> deleteField(
            @PathVariable Long formId,
            @PathVariable Long fieldId) {
        fieldService.deleteField(formId, fieldId);
        return ResponseEntity.noContent().build();
    }
}