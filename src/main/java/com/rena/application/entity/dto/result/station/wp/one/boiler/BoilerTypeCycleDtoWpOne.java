package com.rena.application.entity.dto.result.station.wp.one.boiler;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.boiler.type.BoilerTypeCycle}
 */
public record BoilerTypeCycleDtoWpOne(@NotNull String typeName, @NotNull String article) {
}