package com.rena.application.entity.dto.result.station.wp.one;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WpOneResponse(@NotBlank String serialNumber,
                            @NotNull Integer amountBoilerPrint,
                            @NotNull Integer amountBoilerShift) {
}
