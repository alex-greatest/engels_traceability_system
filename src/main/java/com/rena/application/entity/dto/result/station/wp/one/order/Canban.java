package com.rena.application.entity.dto.result.station.wp.one.order;

import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Canban(
        @NotBlank String id,
        @Min(1) Integer numberOrder,
        @NotBlank @Nonnull String article,
        @NotNull @Nonnull Integer amountBoilerOrder,
        @NotBlank String code,
        @Min(1) Integer numberShift,
        @Min(1) Integer userCode
) {}
