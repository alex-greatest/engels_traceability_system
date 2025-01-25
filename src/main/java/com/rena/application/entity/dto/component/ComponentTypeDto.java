package com.rena.application.entity.dto.component;

import com.rena.application.entity.model.component.ComponentType;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link ComponentType}
 */
public record ComponentTypeDto(Long id,
                               @NotBlank @Nonnull @Size(message = "Длина должна быть больше 1 и меньше 50 символов", min = 1, max = 50)
                               String name) {
}