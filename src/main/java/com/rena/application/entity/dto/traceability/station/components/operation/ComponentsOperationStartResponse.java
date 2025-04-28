package com.rena.application.entity.dto.traceability.station.components.operation;

import com.rena.application.entity.dto.traceability.common.exchange.MainInformation;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.dto.traceability.station.components.scanned.ComponentsScannedOperation;
import com.rena.application.entity.dto.traceability.station.components.scanned.MaterialScannedOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link Boiler}
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ComponentsOperationStartResponse extends RpcBase {
    @NotNull
    private final MainInformation mainInformation;
    @NotNull
    private final BoilerTypeOperation boilerTypeCycle;
    @NotNull
    private final ComponentsScannedOperation componentsScannedOperation;
    @NotNull
    private final MaterialScannedOperation materialScannedOperation;
}