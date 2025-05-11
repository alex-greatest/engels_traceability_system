package com.rena.application.entity.dto.traceability.station.components;

import com.rena.application.entity.dto.settings.component.ComponentTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComponentsResultSave(@NotBlank String componentType,
                                   @NotBlank String scannedValue,
                                   Integer status,
                                   @NotNull boolean isMaterial) {
}
