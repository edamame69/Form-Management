package com.topcv.form.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class SubmissionValidationException extends RuntimeException {

    private final Map<String, String> fieldErrors;

    public SubmissionValidationException(Map<String, String> fieldErrors) {
        super("Submission validation failed");
        this.fieldErrors = fieldErrors;
    }
}