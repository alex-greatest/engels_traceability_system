package com.rena.application.entity.dto.traceability.station.material.result;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class MaterialResult extends RpcBase {
    @NotBlank private final String boilerOrderId;
    @NotBlank private final String serialNumber;
    @NotBlank private final String stationName;
    @NotBlank private final List<MaterialResult> materialResults;
}
