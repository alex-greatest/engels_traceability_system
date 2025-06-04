package com.rena.application.entity.dto.traceability.station.order.log;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SerialLogManualRequest extends RpcBase {
    @NotNull private final String serialNumber;
    @NotNull private final Integer amountPrint;
    @NotNull private final String stationName;

    public SerialLogManualRequest(String serialNumber, Integer amountPrint, String stationName) {
        super();
        this.serialNumber = serialNumber;
        this.amountPrint = amountPrint;
        this.stationName = stationName;
    }
}
