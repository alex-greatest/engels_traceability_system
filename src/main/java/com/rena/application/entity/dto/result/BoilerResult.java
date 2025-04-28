package com.rena.application.entity.dto.result;

import com.rena.application.entity.dto.settings.user.UserHistoryDto;
import com.rena.application.entity.model.traceability.common.station.Station;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for {@link Boiler}
 */
public record BoilerResult(@NotNull @Nonnull String serialNumber, @NotNull @Nonnull BoilerTypeCycleDto boilerTypeCycle,
                           @NotNull @Nonnull LocalDateTime dateCreate, @NotNull @Nonnull LocalDateTime dateUpdate, Integer status,
                           @NotNull @Nonnull Station lastStation, @NotNull @Nonnull UserHistoryDto userHistory) {
}