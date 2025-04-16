package com.rena.application.entity.dto.traceability.station.wp;

import jakarta.validation.constraints.NotNull;

public record WpResponse(@NotNull Integer amountBoilerShiftMade) {
}
