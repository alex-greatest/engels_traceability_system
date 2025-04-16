package com.rena.application.entity.dto.traceability.station.wp.one.traceability;

import com.rena.application.entity.dto.traceability.common.BoilerTypeCycleStation;
import com.rena.application.entity.model.traceability.common.Boiler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO for {@link Boiler}
 */
public record BoilerResponseWpOne(@NotBlank String serialNumber,
                                  @NotNull BoilerTypeCycleStation boilerTypeCycle,
                                  @NotNull LocalDateTime dateCreate) {
}