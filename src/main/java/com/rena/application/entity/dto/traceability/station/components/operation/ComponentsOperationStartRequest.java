package com.rena.application.entity.dto.traceability.station.components.operation;

import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link Boiler}
 */
public record ComponentsOperationStartRequest(@Min(1) Integer numberShift,
                                              @Min(1) Integer userCode,
                                              @NotBlank String serialNumber,
                                              @NotBlank String stationName,
                                              @NotBlank String prevStationName,
                                              @NotNull Boolean isAllowStart) {
}