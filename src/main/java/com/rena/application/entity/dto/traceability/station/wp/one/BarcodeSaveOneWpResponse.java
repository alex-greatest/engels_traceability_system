package com.rena.application.entity.dto.traceability.station.wp.one;

import jakarta.validation.constraints.NotNull;

public record BarcodeSaveOneWpResponse(@NotNull Integer amountBoilerPrint,
                                       @NotNull Integer amountBoilerShift) {
}
