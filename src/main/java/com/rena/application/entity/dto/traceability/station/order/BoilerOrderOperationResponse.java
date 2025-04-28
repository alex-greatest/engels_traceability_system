package com.rena.application.entity.dto.traceability.station.order;

import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * DTO for {@link BoilerOrder}
 */
public record BoilerOrderOperationResponse(@NotBlank String id,
                                           Boolean isDataExists,
                                           Integer orderNumber,
                                           String article,
                                           Integer amountBoilerOrder,
                                           Integer amountBoilerPrint,
                                           String codeScan,
                                           LocalDateTime dateScan) {
}