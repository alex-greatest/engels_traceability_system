package com.rena.application.entity.dto.user;

import com.rena.application.entity.model.user.User;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link User}
 */
public record UserRequestPassword(@NotBlank(message = "Пароль не может быть пустым") String password,
                                  @NotBlank(message = "Поле не может быть пустым") String repeatPassword) {
}