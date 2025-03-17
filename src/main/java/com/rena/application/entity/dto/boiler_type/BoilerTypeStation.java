package com.rena.application.entity.dto.boiler_type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.boiler.type.BoilerType}
 */
public record BoilerTypeStation(@NotNull Long id, @NotNull @NotBlank String typeName, @NotNull @NotBlank String article) {
}