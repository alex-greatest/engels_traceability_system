package com.rena.application.entity.dto.traceability.common.exchange;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationNameData extends RpcBase {
    @NotBlank private final String nameStation;

    public StationNameData(String nameStation) {
        super();
        this.nameStation = nameStation;
    }
}
