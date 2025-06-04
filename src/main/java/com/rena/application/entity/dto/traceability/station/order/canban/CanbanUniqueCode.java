package com.rena.application.entity.dto.traceability.station.order.canban;

import jakarta.validation.constraints.NotBlank;

public record CanbanUniqueCode(
        @NotBlank String id
) {}
