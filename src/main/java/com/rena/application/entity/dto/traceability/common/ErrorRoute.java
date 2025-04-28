package com.rena.application.entity.dto.traceability.common;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link Boiler}
 */
public record ErrorRoute(@NotBlank String error, @NotNull String serialNumber, @NotNull Boolean isRouteError) {
}