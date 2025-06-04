package com.rena.application.entity.dto.traceability.station.order.canban;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
public class Canban extends RpcBase {
    @NotBlank private String id;
    @NotBlank private String stationName;
    @Min(1) private final Integer numberOrder;
    @NotBlank private final String article;
    @Min(1) private final Integer amountBoilerOrder;
    @NotBlank private final String mainCode;
    @NotBlank private final String canbanCode;
}
