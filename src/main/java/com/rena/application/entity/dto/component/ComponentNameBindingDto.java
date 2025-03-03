package com.rena.application.entity.dto.component;

import com.rena.application.entity.model.component.set.ComponentNameSet;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link ComponentNameSet}
 */
public record ComponentNameBindingDto(Long id,
                                      @Nonnull @NotBlank @Size(message = "Длина должна быть больше 1 и меньше 50 символов", min = 1, max = 50)
                                      String name) {
}