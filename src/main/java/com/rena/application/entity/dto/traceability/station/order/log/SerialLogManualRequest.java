package com.rena.application.entity.dto.traceability.station.order.log;

import jakarta.validation.constraints.NotNull;

public record SerialLogManualRequest(@NotNull String serialNumber,
                                     @NotNull Integer userCode,
                                     @NotNull Integer shift,
                                     @NotNull Integer amountPrint) {
}
