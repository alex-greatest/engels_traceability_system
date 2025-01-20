package com.rena.application.entity.dto.component;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.component.ComponentSet}
 */
public record ComponentSetDto(Long id, @NotNull(message = "Компонент не может быть null") ComponentDto component,
                              @NotNull(message = "Значение не может быть пустым") String value) {
}