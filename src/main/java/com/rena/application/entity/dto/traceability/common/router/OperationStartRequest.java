package com.rena.application.entity.dto.traceability.common.router;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link Boiler}
 */
public record OperationStartRequest(@NotBlank String serialNumber,
                                    @NotBlank String stationName) {
}