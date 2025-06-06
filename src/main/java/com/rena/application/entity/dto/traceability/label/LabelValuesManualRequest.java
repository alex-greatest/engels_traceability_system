package com.rena.application.entity.dto.traceability.label;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LabelValuesManualRequest extends RpcBase {
    @NotBlank private final String labelName;
    @NotBlank private final String stationName;
    @NotBlank private final String serialNumber;

    public LabelValuesManualRequest(String labelName, String stationName, String serialNumber) {
        super();
        this.labelName = labelName;
        this.stationName = stationName;
        this.serialNumber = serialNumber;
    }
}
