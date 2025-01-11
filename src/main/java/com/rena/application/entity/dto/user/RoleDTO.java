package com.rena.application.entity.dto.user;

import com.rena.application.entity.model.user.Role;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link Role}
 */
public record RoleDTO(@NotBlank(message = "Поле не должно быть пустым") String name) {
}