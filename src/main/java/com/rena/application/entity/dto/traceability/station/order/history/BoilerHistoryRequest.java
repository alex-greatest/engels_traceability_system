package com.rena.application.entity.dto.traceability.station.order.history;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.traceability.common.boiler.Boiler;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * DTO for {@link Boiler}
 */
@Getter
public class BoilerHistoryRequest extends RpcBase {
    @NotBlank final private String id;
    @Min(0) final private Integer page;
    @Min(1) final private Integer size;
    @NotBlank final private String stationName;

    public BoilerHistoryRequest(String id, String stationName, Integer size, Integer page) {
        super();
        this.id = id;
        this.stationName = stationName;
        this.size = size;
        this.page = page;
    }
}