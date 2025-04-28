package com.rena.application.entity.dto.traceability.station.components.scanned;

import com.rena.application.entity.dto.traceability.station.components.scanned.material.MaterialOperation;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record MaterialScannedOperation(@NotNull List<MaterialOperation> materials,
                                       @NotNull List<String> materialsTypeNames) {
}
