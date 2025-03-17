package com.rena.application.entity.dto.result.station.wp.components;

import com.rena.application.entity.model.result.common.Boiler;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link Boiler}
 */
public record ComponentRequest(@Min(1) Integer numberShift,
                               @Min(1) Integer userCode,
                               @NotBlank String serialNumber,
                               @NotBlank String stationName,
                               @NotBlank String prevStationName,
                               @NotNull Boolean isAllowStart) {
}