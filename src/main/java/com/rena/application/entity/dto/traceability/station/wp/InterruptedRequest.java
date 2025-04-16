package com.rena.application.entity.dto.traceability.station.wp;

import com.rena.application.entity.model.traceability.common.Boiler;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link Boiler}
 */
public record InterruptedRequest(@NotBlank String serialNumber,
                                 @NotBlank String stationName,
                                 @NotBlank String message) {
}