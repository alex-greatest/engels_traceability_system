package com.rena.application.entity.dto.result.station.wp.one;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BarcodeGetOneWpRequest(@NotBlank String article, @Min(1) Integer amountPrint) {
}
