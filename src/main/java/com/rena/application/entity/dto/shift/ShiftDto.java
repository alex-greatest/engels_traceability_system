package com.rena.application.entity.dto.shift;

import com.rena.application.entity.model.shift.Shift;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

/**
 * DTO for {@link Shift}
 */
public record ShiftDto(@NotNull @Nonnull Integer number, @NotNull @Nonnull LocalTime timeStart, @NotNull @Nonnull LocalTime timeEnd) {
}