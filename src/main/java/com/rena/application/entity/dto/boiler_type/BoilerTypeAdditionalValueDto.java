package com.rena.application.entity.dto.boiler_type;

import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalData;
import com.rena.application.entity.model.boiler.type.additional.BoilerTypeAdditionalValue;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for {@link BoilerTypeAdditionalValue}
 */
public record BoilerTypeAdditionalValueDto(Long id, String value, String unit,
                                           @NotNull @Nonnull BoilerTypeAdditionalData boilerTypeAdditionalData,
                                           @NotNull @Nonnull BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSet) {
}