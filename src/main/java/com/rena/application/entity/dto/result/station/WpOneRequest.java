package com.rena.application.entity.dto.result.station;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record WpOneRequest(@NotNull @Min(1) Integer orderCode,
                           @NotNull @Min(1) Integer userCode,
                           @NotNull @Min(1) Integer numberShift) {
}
