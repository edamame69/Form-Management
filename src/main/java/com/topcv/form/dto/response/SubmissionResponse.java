package com.topcv.form.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

public record SubmissionResponse(
        Long id,
        Long formId,
        String formTitle,
        Map<String, String> data,
        LocalDateTime submittedAt
) {}