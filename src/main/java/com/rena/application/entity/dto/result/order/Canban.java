package com.rena.application.entity.dto.result.order;

import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record Canban(
        @Min(1) Integer numberOrder,
        @NotBlank @Nonnull String article,
        @NotNull @Nonnull Integer amountBoilerOrder,
        @NotNull @Nonnull LocalDateTime dateScan,     // Может быть null
        @NotBlank String code,
        @Min(1) Integer userCode,
        @Min(1) Integer numberShift
) {}
