package com.topcv.form.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

 //Client gửi data dạng Map<fieldId, value>.

public record SubmitFormRequest(
        @NotNull(message = "Data is required")
        Map<String, String> data
) {}