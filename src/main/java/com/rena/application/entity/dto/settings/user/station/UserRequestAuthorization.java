package com.rena.application.entity.dto.settings.user.station;

import com.rena.application.entity.model.settings.user.User;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link User}
 */
public record UserRequestAuthorization(@NotBlank String login,
                                       @NotBlank String password,
                                       @NotBlank String station) {
}