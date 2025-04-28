package com.rena.application.entity.dto.traceability.station.order.barcode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record BarcodeBoilerOrderSavePrintRequest(
        @NotBlank String id,
        @NotBlank String stationName,
        @NotNull @Min(1) Integer orderCode,
        @NotNull @Min(1) Integer userCode,
        @NotNull @Min(1) Integer numberShift,
        @NotNull List<String> serialNumbers) {
}
