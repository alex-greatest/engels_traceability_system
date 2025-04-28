package com.rena.application.entity.dto.traceability.common.boiler;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class BoilerMadeInformation {
    @NotNull
    private final Integer orderNumber;
    @NotNull
    private final Integer amountBoilerMadeOrder;
    @NotNull
    private final Integer amountBoilerMadeShift;
    @NotNull
    private final Integer shiftNumber;
}
