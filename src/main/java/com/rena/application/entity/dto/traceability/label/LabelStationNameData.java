package com.rena.application.entity.dto.traceability.label;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LabelStationNameData extends RpcBase {
    @NotBlank private final String labelName;
    @NotBlank private final String stationName;

    public LabelStationNameData(String labelName, String stationName) {
        super();
        this.labelName = labelName;
        this.stationName = stationName;
    }
}
