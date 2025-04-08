package com.rena.application.entity.dto.user.station;

import com.rena.application.entity.model.user.User;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link User}
 */
public record OperatorRequestAuthorization(@NotBlank String login,
                                           @NotBlank String password,
                                           @NotBlank String station) {
}