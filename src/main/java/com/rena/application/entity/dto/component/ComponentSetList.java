package com.rena.application.entity.dto.component;

import java.util.List;
import com.rena.application.entity.model.component.set.ComponentSet;
import com.vaadin.hilla.Nonnull;

/**
 * DTO for {@link ComponentSet}
 */
public record ComponentSetList(@Nonnull ComponentNameSetDto componentNameSet,
                               @Nonnull List<@Nonnull ComponentTypeDto> componentsTypeList,
                               @Nonnull List<@Nonnull ComponentSetDto> componentsSet) {
}