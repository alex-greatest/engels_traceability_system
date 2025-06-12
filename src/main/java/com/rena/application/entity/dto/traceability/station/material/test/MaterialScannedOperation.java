package com.rena.application.entity.dto.traceability.station.material.test;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class MaterialScannedOperation {
    @NotNull private List<MaterialTypeStation> materials;
}
