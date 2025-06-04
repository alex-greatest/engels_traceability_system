package com.rena.application.entity.dto.traceability.station.order.barcode;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BarcodeSaveResponse extends RpcBase {
    private final Integer amountPrintLabel;
}
