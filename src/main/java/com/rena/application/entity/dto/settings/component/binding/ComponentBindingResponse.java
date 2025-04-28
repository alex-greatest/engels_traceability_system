package com.rena.application.entity.dto.settings.component.binding;

import com.rena.application.entity.dto.settings.component.ComponentTypeDto;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link com.rena.application.entity.model.settings.component.binding.ComponentBinding}
 */
public record ComponentBindingResponse(Long id,
                                       @Nonnull @NotNull ComponentTypeDto componentType,
                                       @Nonnull @Min(1) Integer order) {
}