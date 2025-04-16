package com.rena.application.entity.dto.traceability.station.wp.one.order;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.rena.application.entity.model.traceability.station.wp.one.order.BoilerOrder}
 */
public record BoilerOrderResponseWpOne(@NotBlank String id,
                                       Boolean isDataExists,
                                       Integer orderNumber,
                                       String article,
                                       Integer amountBoilerOrder,
                                       Integer amountBoilerPrint,
                                       String codeScan,
                                       LocalDateTime dateScan) {
}