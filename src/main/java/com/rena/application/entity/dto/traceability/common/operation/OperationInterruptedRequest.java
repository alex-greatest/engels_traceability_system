package com.rena.application.entity.dto.traceability.common.operation;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link Boiler}
 */
public record OperationInterruptedRequest(@NotBlank String serialNumber,
                                          @NotBlank String stationName,
                                          @NotBlank String message) {
}