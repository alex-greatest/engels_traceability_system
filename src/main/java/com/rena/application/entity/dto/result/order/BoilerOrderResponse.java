package com.rena.application.entity.dto.result.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.rena.application.entity.model.result.order.BoilerOrder}
 */
public record BoilerOrderResponse(@Min(1) Integer orderNumber,
                                  @NotBlank String article,
                                  @NotNull Integer amountBoilerOrder,
                                  @NotNull Integer amountBoilerPrint,
                                  @NotBlank String codeScan,
                                  @NotNull LocalDateTime dateScan) {
}