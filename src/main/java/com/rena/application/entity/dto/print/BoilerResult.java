package com.rena.application.entity.dto.print;

import com.rena.application.entity.dto.user.UserHistoryDto;
import com.rena.application.entity.model.traceability.common.Station;
import com.rena.application.entity.model.traceability.common.Status;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.rena.application.entity.model.traceability.common.Boiler}
 */
public record BoilerResult(@NotNull @Nonnull String serialNumber, @NotNull @Nonnull BoilerTypeCycleDto boilerTypeCycle,
                           @NotNull @Nonnull LocalDateTime dateCreate, @NotNull @Nonnull LocalDateTime dateUpdate, Status status,
                           @NotNull @Nonnull Station lastStation, @NotNull @Nonnull UserHistoryDto userHistory) {
}