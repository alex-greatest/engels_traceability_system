package com.rena.application.entity.dto.traceability.common.router;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class OperationStartRoute extends RpcBase {
    private @Min(1) Integer numberShift;
    private @Min(1) Integer userCode;
    private @NotBlank String serialNumber;
    private @NotBlank String stationName;
}
