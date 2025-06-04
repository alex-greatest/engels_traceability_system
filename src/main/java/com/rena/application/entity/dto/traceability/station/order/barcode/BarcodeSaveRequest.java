package com.rena.application.entity.dto.traceability.station.order.barcode;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BarcodeSaveRequest extends RpcBase {
    @NotBlank private final String station;
    @NotBlank private final String boilerOrderId;
    @NotBlank private final String serialNumber;
    @NotNull private final Integer amountPrint;

    public BarcodeSaveRequest(String station, String serialNumber, Integer amountPrint, String boilerOrderId) {
        super();
        this.station = station;
        this.serialNumber = serialNumber;
        this.amountPrint = amountPrint;
        this.boilerOrderId = boilerOrderId;
    }
}
