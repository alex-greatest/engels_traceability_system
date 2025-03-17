package com.rena.application.entity.dto.result.station.wp;

import jakarta.validation.constraints.NotNull;

public record WpResponse(@NotNull Integer amountBoilerShiftMade) {
}
