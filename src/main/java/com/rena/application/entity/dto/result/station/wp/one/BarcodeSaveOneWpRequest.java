package com.rena.application.entity.dto.result.station.wp.one;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BarcodeSaveOneWpRequest(
        @NotBlank String id,
        @NotNull @Min(1) Integer orderCode,
        @NotNull @Min(1) Integer userCode,
        @NotNull @Min(1) Integer numberShift,
        @NotNull List<String> serialNumbers) {
}
