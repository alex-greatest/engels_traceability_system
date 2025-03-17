package com.rena.application.entity.dto.result.print;

import com.rena.application.entity.dto.result.common.UserHistoryDto;
import com.rena.application.entity.model.boiler.type.BoilerTypeCycle;
import com.rena.application.entity.model.result.common.Status;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO for {@link com.rena.application.entity.model.result.station.wp.one.order.BoilerOrder}
 */
public record BoilerOrderDto(String id, @Nonnull @NotNull String scanCode, @Nonnull @NotNull Status status,
                             @Nonnull @NotNull BoilerTypeCycleDto boilerTypeCycle, @Nonnull Integer orderNumber,
                             @Nonnull @NotNull Integer amountBoilerOrder, @Nonnull @NotNull Integer amountBoilerPrint,
                             @Nonnull @NotNull Integer amountBoilerMade, @Nonnull @NotNull Integer numberShiftCreated,
                             @Nonnull @NotNull LocalDateTime modifiedDate, @Nonnull  @NotNull UserHistoryDto userHistory) {
}