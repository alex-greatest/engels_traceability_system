package com.rena.application.entity.dto.result.station.wp.components;

import com.rena.application.entity.dto.component.ComponentTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComponentsResult(@NotNull ComponentTypeDto componentType,
                               @NotBlank String scannedValue,
                               @NotBlank String status) {
}
