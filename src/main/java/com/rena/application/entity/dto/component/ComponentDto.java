package com.rena.application.entity.dto.component;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for {@link com.rena.application.entity.model.component.Component}
 */
public record ComponentDto(Long id, @NotBlank(message = "Поле не может быть пустым") String name) {
}