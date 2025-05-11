package com.rena.application.entity.dto.traceability.common.router;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorRoute extends RpcBase {
     private final @NotBlank String error;
     private final @NotBlank String serialNumber;
}