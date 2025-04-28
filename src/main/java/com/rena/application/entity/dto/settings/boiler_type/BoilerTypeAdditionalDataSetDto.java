package com.rena.application.entity.dto.settings.boiler_type;

import com.rena.application.entity.model.settings.type.additional.BoilerTypeAdditionalDataSet;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link BoilerTypeAdditionalDataSet}
 */
public record BoilerTypeAdditionalDataSetDto(Long id, @NotNull(message = "Название не может быть пустым") @Nonnull String name) {
}