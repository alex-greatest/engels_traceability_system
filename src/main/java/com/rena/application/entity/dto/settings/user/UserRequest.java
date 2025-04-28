package com.rena.application.entity.dto.settings.user;

import com.rena.application.entity.model.settings.user.User;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link User}
 */
public record UserRequest(@NotNull(message = "Поле не должно быть пустым") Integer code,
                          @NotBlank @Size(message = "Длина должна быть больше 1 и меньше 50 символов", min = 1, max = 50)
                          String username,
                          @NotBlank(message = "Пароль не может быть пустым") String password,
                          @NotBlank(message = "Поле не может быть пустым") String repeatPassword,
                          @Nonnull @NotNull RoleDTO role) {
}