package com.rena.application.entity.dto.component;

import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.component.binding.ComponentBinding}
 */
public record ComponentBindingResponse(Long id,
                                       @Nonnull @NotNull ComponentTypeDto componentType,
                                       @Nonnull @Min(1) Integer order) {
}