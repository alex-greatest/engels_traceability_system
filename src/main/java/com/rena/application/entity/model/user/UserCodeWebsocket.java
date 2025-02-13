package com.rena.application.entity.model.user;

import jakarta.validation.constraints.NotNull;

public record UserCodeWebsocket(@NotNull Integer code, @NotNull String station) {
}
