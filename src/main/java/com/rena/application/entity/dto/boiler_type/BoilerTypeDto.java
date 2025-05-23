package com.rena.application.entity.dto.boiler_type;

import com.rena.application.entity.dto.component.ComponentNameSetDto;
import com.rena.application.entity.model.boiler.type.BoilerType;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link BoilerType}
 */
public record BoilerTypeDto(Long id,
                            @NotNull @Nonnull String typeName,
                            @NotNull @Nonnull String model,
                            @NotNull(message = "Поле не может быть пустым") @Nonnull String article,
                            @NotNull(message = "Поле не может быть пустым") @Nonnull ComponentNameSetDto componentNameSet,
                            @NotNull(message = "Поле не может быть пустым") @Nonnull BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSet) {
}