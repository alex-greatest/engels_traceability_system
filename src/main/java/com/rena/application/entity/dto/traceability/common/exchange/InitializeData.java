package com.rena.application.entity.dto.traceability.common.exchange;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitializeData extends RpcBase {
    @NotBlank
    private final String nameStation;

    public InitializeData(String nameStation) {
        super();
        this.nameStation = nameStation;
    }
}
