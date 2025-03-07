package com.rena.application.entity.dto.component;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.component.binding.ComponentBinding}
 */
public record ComponentBindingRequest(Long id,
                                      @NotNull @Nonnull String stationName,
                                      @NotNull @Nonnull ComponentNameSetDto componentNameSet,
                                      @NotNull @Nonnull ComponentTypeDto componentType,
                                      @Min(1) Integer order) {
}