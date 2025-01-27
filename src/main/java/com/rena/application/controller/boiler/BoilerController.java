package com.rena.application.controller.boiler;

import com.rena.application.entity.dto.boiler.BoilerDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.boiler.BoilerService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RequiredArgsConstructor
@BrowserCallable
@Validated
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class BoilerController {
    private final BoilerService boilerService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<BoilerDto> getAllBoiler() {
        return boilerService.getAllBoilers();
    }

    public void addBoiler(@Nonnull @Valid BoilerDto boilerDto) {
        try {
            boilerService.addBoiler(boilerDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении котла", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateBoiler(@Nonnull @Valid BoilerDto boilerDto) {
        try {
            boilerService.addBoiler(boilerDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при обновлении котла", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteBoiler(@Nonnull @NotNull Long id) {
        try {
            boilerService.deleteComponent(id);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
