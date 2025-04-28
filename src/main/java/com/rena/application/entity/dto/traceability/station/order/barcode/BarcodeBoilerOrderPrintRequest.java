package com.rena.application.entity.dto.traceability.station.order.barcode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BarcodeBoilerOrderPrintRequest(@NotBlank String article, @Min(1) Integer amountPrint) {
}
