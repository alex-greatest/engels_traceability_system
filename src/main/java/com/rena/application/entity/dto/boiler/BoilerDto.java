package com.rena.application.entity.dto.boiler;

import com.rena.application.entity.dto.component.ComponentNameSetDto;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.boiler.Boiler}
 */
public record BoilerDto(Long id, @NotNull @Nonnull String name, @NotNull @Nonnull String article,
                        @NotNull(message = "Поле не может быть пустым") @Nonnull ComponentNameSetDto componentNameSet) {
}