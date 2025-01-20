package com.rena.application.entity.dto.component;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for {@link com.rena.application.entity.model.component.ComponentSet}
 */
public record ComponentSetDtos(@NotNull(message = "Название набора не может быть пустым") String componentNameSet,
                               @NotNull(message = "Список не может быть пустым") List<ComponentSetDto> components) {
}