package com.rena.application.entity.dto.result;

import com.rena.application.entity.dto.settings.user.UserHistoryDto;
import com.rena.application.entity.model.traceability.station.order.BoilerOrder;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO for {@link BoilerOrder}
 */
public record BoilerOrderDto(String id, @Nonnull @NotNull String scanCode, @Nonnull @NotNull Integer status,
                             @Nonnull @NotNull BoilerTypeCycleDto boilerTypeCycle, @Nonnull Integer orderNumber,
                             @Nonnull @NotNull Integer amountBoilerOrder, @Nonnull @NotNull Integer amountBoilerPrint,
                             @Nonnull @NotNull Integer amountBoilerMade, @Nonnull @NotNull Integer numberShiftCreated,
                             @Nonnull @NotNull LocalDateTime modifiedDate, @Nonnull  @NotNull UserHistoryDto userHistory) {
}