package com.rena.application.entity.dto.result.station.wp.one.traceability;

import com.rena.application.entity.model.result.common.Boiler;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link Boiler}
 */
public record BoilerRequestWpOne(@NotBlank String id, @Min(1) Integer orderNumber, @Min(0) Integer page, @Min(1) Integer size,
                                 @NotBlank String destinationResponse, @NotBlank String destinationResponseError) {

}