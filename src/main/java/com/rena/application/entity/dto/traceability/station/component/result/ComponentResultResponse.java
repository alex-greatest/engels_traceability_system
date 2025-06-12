package com.rena.application.entity.dto.traceability.station.component.result;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.traceability.station.component.Component;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * DTO for {@link Component}
 */
@Getter
public class ComponentResultResponse extends RpcBase {
    @NotBlank private final String name;
    @NotBlank private final String value;
    @NotBlank private final Integer resultStatus;
    private final String message;

    public ComponentResultResponse(String name, String value, Integer resultStatus, String message) {
        super();
        this.name = name;
        this.value = value;
        this.resultStatus = resultStatus;
        this.message = message;
    }
}