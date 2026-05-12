package com.topcv.form.controller;

import com.topcv.form.dto.request.SubmitFormRequest;
import com.topcv.form.dto.response.ErrorResponse;
import com.topcv.form.dto.response.FormResponse;
import com.topcv.form.dto.response.SubmissionResponse;
import com.topcv.form.service.SubmissionService;
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
@RequiredArgsConstructor
@Tag(name = "3. Submission", description = "APIs cho nhân viên SW - xem form active và submit data")
public class SubmissionController {

    private final SubmissionService submissionService;

    @GetMapping("/api/forms/active")
    @Operation(
            summary = "Lấy danh sách form active (cho nhân viên)",
            description = "Trả về danh sách form có status=ACTIVE, kèm fields, sắp xếp theo displayOrder ASC."
    )
    public ResponseEntity<List<FormResponse>> getActiveForms() {
        return ResponseEntity.ok(submissionService.getActiveForms());
    }

    @PostMapping("/api/forms/{id}/submit")
    @Operation(
            summary = "Submit form với dữ liệu của các field",
            description = """
                    Nhân viên submit form với data dạng Map<fieldId, value>.
                    
                    Hệ thống validate dữ liệu theo cấu hình từng field. 
                    Nếu có lỗi sẽ trả về toàn bộ lỗi (không dừng ở lỗi đầu tiên) trong fieldErrors.
                    
                    Chỉ form có status=ACTIVE mới được submit.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Submit thành công"),
            @ApiResponse(responseCode = "400", description = "Validation lỗi hoặc form không active",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Form không tồn tại")
    })
    public ResponseEntity<SubmissionResponse> submitForm(
            @PathVariable Long id,
            @Valid @RequestBody SubmitFormRequest request) {
        SubmissionResponse response = submissionService.submitForm(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/api/submissions")
    @Operation(
            summary = "Lấy danh sách tất cả bài đã submit",
            description = "Sắp xếp theo thời gian submit mới nhất trước."
    )
    public ResponseEntity<List<SubmissionResponse>> getAllSubmissions() {
        return ResponseEntity.ok(submissionService.getAllSubmissions());
    }
}