package com.rena.application.entity.dto.result.station.wp.one.boiler;

import com.rena.application.entity.model.result.station.wp.one.Boiler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO for {@link Boiler}
 */
public record BoilerResponseWpOne(@NotBlank String serialNumber,
                                  @NotNull BoilerTypeCycleDtoWpOne boilerTypeCycle,
                                  @NotNull LocalDateTime dateCreate) {
}