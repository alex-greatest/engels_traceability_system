package com.rena.application.entity.dto.traceability.common.boiler;

import jakarta.validation.constraints.NotNull;

public record BoilerMadeInformation(@NotNull Integer orderNumber, @NotNull Integer amountBoilerMadeOrder,
                                    @NotNull Integer amountBoilerMadeShift, @NotNull Integer shiftNumber) {
}
