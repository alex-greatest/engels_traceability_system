package com.rena.application.entity.dto.component;

import com.rena.application.entity.model.component.binding.ComponentBinding;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link ComponentBinding}
 */
public record ComponentBindingDto(Long id,
                                  @NotNull @Nonnull ComponentTypeDto componentType,
                                  @Min(message = "Длина должна быть больше 1", value = 1)
                                  @Nonnull Integer order) {
}