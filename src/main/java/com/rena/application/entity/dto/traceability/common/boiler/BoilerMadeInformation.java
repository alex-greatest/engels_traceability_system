package com.rena.application.entity.dto.traceability.common.boiler;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BoilerMadeInformation extends RpcBase {
    @NotNull private Integer orderNumber;
    @NotNull private Integer amountBoilerMadeOrder;
    @NotNull private Integer amountBoilerOrder;
}
