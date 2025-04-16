package com.rena.application.entity.dto.print;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.traceability.common.Status}
 */
public record StatusDto(@NotNull Long id, @NotNull String name) {
}