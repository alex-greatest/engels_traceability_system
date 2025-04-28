package com.rena.application.entity.dto.traceability.station.order.history;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO for {@link Boiler}
 */
public record BoilerHistoryResponse(@NotBlank String serialNumber,
                                    @NotNull BoilerTypeOperation boilerTypeCycle,
                                    @NotNull LocalDateTime dateCreate) {
}