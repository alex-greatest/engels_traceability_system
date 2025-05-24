package com.rena.application.entity.dto.traceability.common.operation;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link Boiler}
 */
@Getter
@Setter
@RequiredArgsConstructor
public class OperationInterruptedRequest extends RpcBase {
    @NotBlank private final String serialNumber;
    @NotBlank private final String stationName;
    @NotBlank private final String message;
    private final Integer adminInterrupted;
}