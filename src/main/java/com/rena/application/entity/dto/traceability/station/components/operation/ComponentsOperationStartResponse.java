package com.rena.application.entity.dto.traceability.station.components.operation;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.dto.traceability.station.components.scanned.ComponentsMaterials;
import com.rena.application.entity.dto.traceability.station.components.scanned.ComponentsScannedOperation;
import com.rena.application.entity.dto.traceability.station.components.scanned.MaterialScannedOperation;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * DTO for {@link Boiler}
 */
@Getter
@Setter
@RequiredArgsConstructor
public class ComponentsOperationStartResponse extends RpcBase {
    @NotNull
    private final BoilerMadeInformation boilerMadeInformation;
    @NotNull
    private final BoilerTypeOperation boilerTypeCycle;
    @NotNull
    private final ComponentsScannedOperation componentsScannedOperation;
    @NotNull
    private final MaterialScannedOperation materialScannedOperation;
    @NotNull
    private final List<ComponentsMaterials> componentsMaterials;
}