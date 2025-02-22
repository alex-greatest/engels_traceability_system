package com.rena.application.entity.sql;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BoilerOrderSQL(@NotNull Long amountBoilerMade, @NotNull Long amountBoilerOrder,
                             @NotNull Long amountBoilerPrint, @Min(1) Long boilerTypeId, @NotBlank String article) {
}
