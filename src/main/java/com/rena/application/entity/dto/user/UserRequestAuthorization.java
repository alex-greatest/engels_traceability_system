package com.rena.application.entity.dto.user;

import com.rena.application.entity.model.user.User;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link User}
 */
public record UserRequestAuthorization(@NotBlank String login,
                                       @NotBlank String password,
                                       @NotBlank String station) {
}