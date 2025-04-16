package com.rena.application.entity.dto.print;

import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.traceability.station.wp.two.Component}
 */
public record ComponentResult(Long id, @Nonnull @NotNull String name, @Nonnull @NotNull String value, @Nonnull @NotNull StatusDto status) {
}