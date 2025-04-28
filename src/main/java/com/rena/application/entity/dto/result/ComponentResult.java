package com.rena.application.entity.dto.result;

import com.rena.application.entity.model.traceability.station.component.Component;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link Component}
 */
public record ComponentResult(Long id, @Nonnull @NotNull String name, @Nonnull @NotNull String value, @Nonnull @NotNull Integer status) {
}