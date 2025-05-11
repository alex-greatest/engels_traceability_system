package com.rena.application.entity.dto.traceability.common.router;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class OperationStartRoute extends RpcBase {
    private @NotBlank String serialNumber;
    private @NotBlank String stationName;
}
