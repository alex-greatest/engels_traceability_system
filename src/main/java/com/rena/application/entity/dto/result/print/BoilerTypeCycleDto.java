package com.rena.application.entity.dto.result.print;

import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.boiler.type.BoilerTypeCycle}
 */
public record BoilerTypeCycleDto(Long id, @Nonnull @NotNull Long boilerTypeId,
                                 @Nonnull @NotBlank String typeName,
                                 @Nonnull @NotBlank String model,
                                 @Nonnull @NotBlank String article) {
}