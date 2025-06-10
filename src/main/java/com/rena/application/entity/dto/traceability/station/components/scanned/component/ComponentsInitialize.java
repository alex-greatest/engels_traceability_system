package com.rena.application.entity.dto.traceability.station.components.scanned.component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComponentsInitialize(@NotBlank String name, @NotBlank String value, @NotNull Integer status) {
}
