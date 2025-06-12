package com.rena.application.entity.dto.traceability.station.component.operation;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.station.component.ComponentsResultSave;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ComponentsOperationSaveResultRequest extends RpcBase {
    @NotNull private final List<ComponentsResultSave> componentsResultSave;
    @NotBlank private final String serialNumber;
    @NotBlank private final String stationName;
    @NotNull private final Boolean isIgnoringError;
    private final Integer adminIgnoringError;
}
