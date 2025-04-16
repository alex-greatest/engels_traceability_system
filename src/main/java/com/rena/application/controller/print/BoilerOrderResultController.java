package com.rena.application.controller.print;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.annotation.Validated;
import com.rena.application.entity.dto.print.BoilerOrderDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.print.BoilerOrderService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@BrowserCallable
@Validated
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class BoilerOrderResultController {
    private final BoilerOrderService boilerOrderService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull BoilerOrderDto> getAllBoilerOrders() {
        try {
            return boilerOrderService.getAllBoilerOrders();
        } catch (DataAccessException e) {
            log.error("Ошибка при получении заказов котлов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    @Nonnull
    public List<@Nonnull BoilerOrderDto> getBoilerOrdersByDateRange(
            @Nonnull LocalDateTime startDate, @Nonnull LocalDateTime endDate) {
        try {
            return boilerOrderService.getBoilerOrdersByDateRange(startDate, endDate);
        } catch (DataAccessException e) {
            log.error("Ошибка при получении заказов котлов за промежуток времени", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
