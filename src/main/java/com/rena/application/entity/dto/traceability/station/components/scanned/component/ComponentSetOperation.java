package com.rena.application.entity.dto.traceability.station.components.scanned.component;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.settings.component.set.ComponentSet}
 */
public record ComponentSetOperation(Long id, @NotNull String componentTypeName, @NotNull String value) {
}