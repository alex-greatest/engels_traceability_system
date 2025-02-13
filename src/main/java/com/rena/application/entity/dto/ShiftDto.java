package com.rena.application.entity.dto;

import com.rena.application.entity.model.Shift;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * DTO for {@link Shift}
 */
public record ShiftDto(@NotNull @Nonnull Long number, @NotNull @Nonnull LocalTime timeStart,  @NotNull @Nonnull LocalTime timeEnd) {
}