package com.rena.application.entity.dto.result.print;

import com.rena.application.entity.model.result.common.Status;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.result.station.wp.two.Component}
 */
public record ComponentDto(Long id, @Nonnull @NotNull String name, @Nonnull @NotNull String value, @Nonnull @NotNull Status status) {
}