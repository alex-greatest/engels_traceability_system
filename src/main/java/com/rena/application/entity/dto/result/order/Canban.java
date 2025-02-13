package com.rena.application.entity.dto.result.order;

import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record Canban(
        @NotNull @Nonnull Long numberOrder,
        @NotBlank @Nonnull String article,
        @NotNull @Nonnull Integer amountBoiler,
        @NotNull @Nonnull LocalDateTime dateScan,     // Может быть null
        @NotBlank @Nonnull String code,
        @NotNull @Nonnull Integer userCode
) {}
