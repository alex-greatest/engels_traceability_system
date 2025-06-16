package com.rena.application.entity.dto.traceability.station.material.operation;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.common.boiler.BoilerTypeOperation;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.dto.traceability.station.material.test.MaterialScannedOperation;
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
public class MaterialsOperationStartResponse extends RpcBase {
    @NotNull private final BoilerMadeInformation boilerMadeInformation;
    @NotNull private final BoilerTypeOperation boilerTypeCycle;
    @NotNull private final MaterialScannedOperation materialScannedOperation;
    @NotNull private final Boolean isPackagingLabelSaved;
}