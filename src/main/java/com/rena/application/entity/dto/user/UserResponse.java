package com.rena.application.entity.dto.user;

import com.rena.application.entity.model.user.User;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link User}
 */
public record UserResponse(@NotBlank @Size(max = 50) String username, @NotNull Integer code,
                           @Nonnull @NotNull RoleDTO role) {
}