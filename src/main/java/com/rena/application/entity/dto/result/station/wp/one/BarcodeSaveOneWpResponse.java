package com.rena.application.entity.dto.result.station.wp.one;

import jakarta.validation.constraints.NotNull;

public record BarcodeSaveOneWpResponse(@NotNull Integer amountBoilerPrint,
                                       @NotNull Integer amountBoilerShift) {
}
