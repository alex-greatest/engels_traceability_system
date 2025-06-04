package com.rena.application.entity.dto.traceability.station.order;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * DTO for {@link BoilerOrder}
 */
@Getter
@RequiredArgsConstructor
public class BoilerOrderOperationResponse extends RpcBase {
    @NotBlank private final String id;
    @NotNull private final Integer orderNumber;
    @NotBlank private final String article;
    @NotNull private final Integer amountBoilerOrder;
    @NotNull private final Integer amountBoilerPrint;
    @NotBlank private final String mainCode;
    @NotBlank private final String canbanCode;
}