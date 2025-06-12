package com.rena.application.entity.dto.traceability.station.component;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComponentsResultSave(@NotBlank String name,
                                   @NotBlank String scannedValue,
                                   @NotNull String scannedCode,
                                   Integer status) {
}
