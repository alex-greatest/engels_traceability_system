package com.rena.application.entity.dto.traceability.station.order.barcode;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class BarcodeGenerated extends RpcBase {
    @NotBlank final private List<String> barcodes;
}
