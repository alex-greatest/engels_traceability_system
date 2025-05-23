package com.rena.application.entity.dto.result.station.wp.one.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CanbanUniqueCode(
        @Min(1) Integer userCode,
        @NotBlank String id,
        @Min(1) Integer numberShift
) {}
