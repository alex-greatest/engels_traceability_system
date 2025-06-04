package com.rena.application.entity.dto.traceability.station.order.barcode;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BarcodeBoilerGetRequest extends RpcBase {
    @NotBlank private final String station;
    @NotBlank private final String article;
    @NotBlank private final String typeLabel;

    public BarcodeBoilerGetRequest(String station, String article, String typeLabel) {
        super();
        this.station = station;
        this.article = article;
        this.typeLabel = typeLabel;
    }
}