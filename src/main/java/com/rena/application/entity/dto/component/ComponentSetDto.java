package com.rena.application.entity.dto.component;

import com.rena.application.entity.model.component.ComponentSet;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


/**
 * DTO for {@link ComponentSet}
 */
public record ComponentSetDto(Long id,
                              @NotNull @Nonnull ComponentTypeDto componentType,
                              @NotBlank @Nonnull @Size(message = "Длина должна быть больше 1 и меньше 50 символов", min = 1, max = 50)
                              String value) {
}