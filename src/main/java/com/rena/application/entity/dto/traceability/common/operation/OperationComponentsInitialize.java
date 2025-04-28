package com.rena.application.entity.dto.traceability.common.operation;

import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import com.rena.application.entity.dto.traceability.station.components.operation.ComponentsOperationStartResponse;

public record OperationComponentsInitialize(BoilerMadeInformation boilerMadeInformation,
                                            ComponentsOperationStartResponse componentsOperationStartResponse,
                                            Integer numberShift) {
}
