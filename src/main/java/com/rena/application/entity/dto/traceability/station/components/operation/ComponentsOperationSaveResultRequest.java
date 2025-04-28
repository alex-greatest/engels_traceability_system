package com.rena.application.entity.dto.traceability.station.components.operation;

import com.rena.application.entity.dto.traceability.station.components.ComponentsResultSave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ComponentsOperationSaveResultRequest(@NotNull List<ComponentsResultSave> componentsResultSave,
                                                   @NotBlank String serialNumber,
                                                   @NotBlank String stationName,
                                                   @NotBlank Integer status) {
}
