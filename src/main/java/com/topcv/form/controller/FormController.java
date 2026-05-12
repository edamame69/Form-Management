package com.topcv.form.controller;

import com.topcv.form.dto.request.FormRequest;
import com.topcv.form.dto.response.ErrorResponse;
import com.topcv.form.dto.response.FormResponse;
import com.topcv.form.service.FormService;
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

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
@Tag(name = "1. Form Management", description = "Admin APIs - Quản lý form (tạo, sửa, xóa, xem)")
public class FormController {

    private final FormService formService;

    @GetMapping
    @Operation(
            summary = "Lấy danh sách tất cả form",
            description = "Trả về danh sách form (không kèm fields). Dùng cho trang quản lý của admin."
    )
    @ApiResponse(responseCode = "200", description = "Lấy danh sách thành công")
    public ResponseEntity<List<FormResponse>> getAllForms() {
        return ResponseEntity.ok(formService.getAllForms());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Lấy chi tiết 1 form (kèm danh sách field)",
            description = "Dùng khi admin xem hoặc sửa form. Field được sắp xếp theo fieldOrder ASC."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tìm thấy form"),
            @ApiResponse(responseCode = "404", description = "Form không tồn tại",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FormResponse> getFormById(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFormById(id));
    }

    @PostMapping
    @Operation(
            summary = "Tạo form mới",
            description = "Tạo form với title, description, displayOrder và status. Fields được thêm riêng qua API khác."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tạo thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<FormResponse> createForm(@Valid @RequestBody FormRequest request) {
        FormResponse created = formService.createForm(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Cập nhật thông tin form")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cập nhật thành công"),
            @ApiResponse(responseCode = "400", description = "Dữ liệu không hợp lệ"),
            @ApiResponse(responseCode = "404", description = "Form không tồn tại")
    })
    public ResponseEntity<FormResponse> updateForm(
            @PathVariable Long id,
            @Valid @RequestBody FormRequest request) {
        return ResponseEntity.ok(formService.updateForm(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Xóa form",
            description = "Xóa form sẽ tự động xóa tất cả field con (cascade)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Xóa thành công"),
            @ApiResponse(responseCode = "404", description = "Form không tồn tại")
    })
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }
}