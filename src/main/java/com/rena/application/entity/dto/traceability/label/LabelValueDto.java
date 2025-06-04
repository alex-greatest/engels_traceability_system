package com.rena.application.entity.dto.traceability.label;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link com.rena.application.entity.model.traceability.label.LabelValue}
 */
public record LabelValueDto(@NotBlank String variable, @NotBlank String value) {
}