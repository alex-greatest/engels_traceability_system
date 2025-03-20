package com.rena.application.entity.dto.result.print;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.result.common.Status}
 */
public record StatusDto(@NotNull Long id, @NotNull String name) {
}