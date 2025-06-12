package com.rena.application.entity.dto.traceability.station.material;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MaterialsResultSave(@NotBlank String name,
                                  @NotBlank String scannedValue,
                                  @NotNull String scannedCode,
                                  Integer status) {
}
