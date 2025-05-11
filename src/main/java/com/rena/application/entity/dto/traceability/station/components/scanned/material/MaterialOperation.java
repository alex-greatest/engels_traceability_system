package com.rena.application.entity.dto.traceability.station.components.scanned.material;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link com.rena.application.entity.model.settings.component.material.Material}
 */
public record MaterialOperation(@NotBlank String name, @NotBlank String value) {
}