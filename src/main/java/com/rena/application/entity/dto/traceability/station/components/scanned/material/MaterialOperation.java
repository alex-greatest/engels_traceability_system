package com.rena.application.entity.dto.traceability.station.components.scanned.material;

import com.rena.application.entity.model.settings.component.material.Material;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link Material}
 */
public record MaterialOperation(@NotBlank String name, @NotBlank String value) {
}