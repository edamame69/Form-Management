package com.topcv.form.dto.request;

import com.topcv.form.enums.FormStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record FormRequest(
        @Schema(description = "Tiêu đề form", example = "Đăng ký nghỉ phép")
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @Schema(description = "Mô tả ngắn", example = "Form xin nghỉ phép cho nhân viên SW")
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Schema(description = "Thứ tự hiển thị (số nhỏ hiển thị trước)", example = "1")
        @NotNull(message = "Display order is required")
        @Min(value = 0, message = "Display order must be >= 0")
        Integer displayOrder,

        @Schema(description = "Trạng thái form: ACTIVE (nhân viên thấy được) hoặc DRAFT", example = "ACTIVE")
        @NotNull(message = "Status is required")
        FormStatus status
) {}