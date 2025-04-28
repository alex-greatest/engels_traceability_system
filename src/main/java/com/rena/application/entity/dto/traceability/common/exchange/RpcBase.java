package com.rena.application.entity.dto.traceability.common.exchange;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RpcBase {
    @NotBlank
    private String correlationId;
}
