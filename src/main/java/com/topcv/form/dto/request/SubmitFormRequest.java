package com.topcv.form.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record SubmitFormRequest(
        @Schema(
                description = "Map dữ liệu theo field ID. Key là ID của field (dạng chuỗi), value là giá trị do nhân viên nhập.",
                example = """
                        {
                          "1": "Nguyễn Văn A",
                          "2": "5",
                          "3": "2026-12-25",
                          "4": "Cá nhân"
                        }
                        """
        )
        @NotNull(message = "Data is required")
        Map<String, String> data
) {}