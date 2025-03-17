package com.rena.application.entity.dto.result.common;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.boiler.type.BoilerTypeCycle}
 */
public record BoilerTypeCycleStation(@NotNull String typeName,
                                     @NotNull String article,
                                     @NotNull String serialNumber) {
}