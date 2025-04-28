package com.rena.application.entity.dto.traceability.station.order.barcode;

import jakarta.validation.constraints.NotNull;

public record BarcodeBoilerOrderSavePrintResponse(@NotNull Integer amountBoilerPrint,
                                                  @NotNull Integer amountBoilerShift) {
}
