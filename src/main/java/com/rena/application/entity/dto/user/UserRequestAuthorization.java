package com.rena.application.entity.dto.user;

import com.rena.application.entity.model.user.User;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link User}
 */
public record UserRequestAuthorization(@NotBlank String login,
                                       @NotBlank String password,
                                       @NotBlank String station) {
}