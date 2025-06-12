package com.rena.application.entity.dto.traceability.station.material.result;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.traceability.station.component.Component;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * DTO for {@link Component}
 */
@Getter
public class MaterialResultRequest extends RpcBase {
    @NotBlank private final String name;
    @NotBlank private final String code;
    @NotBlank private final String value;
    @NotBlank private final String serialNumber;
    @NotBlank private final String stationName;

    public MaterialResultRequest(String name, String value, String serialNumber, String stationName, String code) {
        super();
        this.name = name;
        this.value = value;
        this.serialNumber = serialNumber;
        this.stationName = stationName;
        this.code = code;
    }
}