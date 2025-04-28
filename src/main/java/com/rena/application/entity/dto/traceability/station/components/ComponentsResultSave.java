package com.rena.application.entity.dto.traceability.station.components;

import com.rena.application.entity.dto.settings.component.ComponentTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComponentsResultSave(@NotNull ComponentTypeDto componentType,
                                   @NotBlank String scannedValue,
                                   @NotBlank String status,
                                   @NotNull boolean isComponent) {
}
