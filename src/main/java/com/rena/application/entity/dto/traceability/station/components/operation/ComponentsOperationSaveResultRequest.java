package com.rena.application.entity.dto.traceability.station.components.operation;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.station.components.ComponentsResultSave;
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
    @NotBlank private final Integer status;
    private final String ignoringMessage;
}
