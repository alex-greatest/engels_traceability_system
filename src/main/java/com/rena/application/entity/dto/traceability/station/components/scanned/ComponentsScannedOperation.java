package com.rena.application.entity.dto.traceability.station.components.scanned;

import com.rena.application.entity.dto.traceability.station.components.scanned.component.ComponentSetOperation;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ComponentsScannedOperation(@NotNull List<ComponentSetOperation> components,
                                         @NotNull List<String> componentTypeNames) {
}
