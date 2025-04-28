package com.rena.application.entity.dto.traceability.common.boiler;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.settings.type.BoilerTypeCycle}
 */
public record BoilerTypeOperation(@NotNull String typeName,
                                  @NotNull String article,
                                  @NotNull String serialNumber) {
}