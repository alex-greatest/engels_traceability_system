package com.rena.application.entity.dto.result.station.wp.one.traceability;

import jakarta.validation.constraints.NotNull;

public record SerialLogManualRequest(@NotNull String serialNumber,
                                     @NotNull Integer userCode,
                                     @NotNull Integer shift,
                                     @NotNull Integer amountPrint) {
}
