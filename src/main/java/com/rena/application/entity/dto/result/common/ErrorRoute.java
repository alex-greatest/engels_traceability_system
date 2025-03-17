package com.rena.application.entity.dto.result.common;

import com.rena.application.entity.model.result.common.Boiler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link Boiler}
 */
public record ErrorRoute(@NotBlank String error, @NotNull String serialNumber, @NotNull Boolean isRouteError) {
}