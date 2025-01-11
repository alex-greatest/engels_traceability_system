package com.rena.application.entity.dto.user;

import com.rena.application.entity.model.user.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link User}
 */
public record UserResponse(@NotNull @Size(max = 50) String username, @NotNull Integer code,
                           @NotNull RoleDTO role) {
}