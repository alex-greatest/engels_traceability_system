package com.rena.application.entity.dto.traceability.station.component.test;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class ComponentsScannedOperation {
    @NotNull private List<ComponentTypeStation> components;
}
