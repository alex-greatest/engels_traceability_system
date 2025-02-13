package com.rena.application.controller.boiler.type.additional;

import com.rena.application.entity.dto.boiler_type.BoilerTypeAdditionalDataSetDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.boiler.type.additional.BoilerTypeAdditionalDataSetService;
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
public class BoilerTypeAdditionalSetController {
    private final BoilerTypeAdditionalDataSetService boilerTypeAdditionalDataSetService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull BoilerTypeAdditionalDataSetDto> getAllDataSet() {
        return boilerTypeAdditionalDataSetService.getAllDataSet();
    }

    @Nonnull
    public BoilerTypeAdditionalDataSetDto getDataSet(@Nonnull Long id)
    {
        return boilerTypeAdditionalDataSetService.getDataSet(id);
    }

    public void addDataSet(@Nonnull @Valid BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSetDto) {
        try {
            boilerTypeAdditionalDataSetService.addDataSet(boilerTypeAdditionalDataSetDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении набора данных котла", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateDataSet(@Nonnull @Valid BoilerTypeAdditionalDataSetDto boilerTypeAdditionalDataSetDto) {
        try {
            boilerTypeAdditionalDataSetService.updateDataSet(boilerTypeAdditionalDataSetDto);
        }
        catch (DataAccessException e) {
            log.error("Ошибка при обновлении набора данных котла", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteDataSet(@Nonnull @NotNull Long id) {
        try {
            boilerTypeAdditionalDataSetService.deleteDataSet(id);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении набора данных котла", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
