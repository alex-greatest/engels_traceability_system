package com.rena.application.entity.dto.result.station.wp.one.boiler;

import com.rena.application.entity.dto.result.common.BoilerTypeCycleStation;
import com.rena.application.entity.model.result.common.Boiler;
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