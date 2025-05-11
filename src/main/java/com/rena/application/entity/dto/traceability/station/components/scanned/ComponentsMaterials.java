package com.rena.application.entity.dto.traceability.station.components.scanned;

import jakarta.validation.constraints.NotBlank;

public record ComponentsMaterials(@NotBlank String type, @NotBlank String value) {
}
