package com.rena.application.controller.result.print;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.annotation.Validated;
import com.rena.application.entity.dto.result.print.BoilerResult;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.result.print.BoilerResultService;
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
public class BoilerResultController {

    private final BoilerResultService boilerResultService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull BoilerResult> getAllBoilers() {
        try {
            return boilerResultService.getAllBoilers();
        } catch (DataAccessException e) {
            log.error("Ошибка при получении котлов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    @Nonnull
    public List<@Nonnull BoilerResult> getBoilersByDateRange(@Nonnull LocalDateTime startDate,
            @Nonnull LocalDateTime endDate) {
        try {
            return boilerResultService.getBoilersByDateCreateRange(startDate, endDate);
        } catch (DataAccessException e) {
            log.error("Ошибка при получении котлов за промежуток времени", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    @Nonnull
    public List<@Nonnull BoilerResult> getBoilerById(@Nonnull Long boilerId) {
        try {
            return boilerResultService.getBoilersById(boilerId);
        } catch (DataAccessException e) {
            log.error("Ошибка при получении котлов по идентификатору", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
