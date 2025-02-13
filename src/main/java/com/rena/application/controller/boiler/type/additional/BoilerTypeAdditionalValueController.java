package com.rena.application.controller.boiler.type.additional;

import com.rena.application.entity.dto.boiler_type.BoilerTypeAdditionalValueDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.boiler.type.additional.BoilerTypeAdditionalValueService;
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
public class BoilerTypeAdditionalValueController {
    private final BoilerTypeAdditionalValueService boilerTypeAdditionalValueService;
    private HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull BoilerTypeAdditionalValueDto> getAllAdditionalValue(@Nonnull @NotNull Long boilerTypeAdditionalDataSetId) {
        return boilerTypeAdditionalValueService.getAllValue(boilerTypeAdditionalDataSetId);
    }

    public void updateAdditionalValue(@Nonnull @Valid List<@Nonnull BoilerTypeAdditionalValueDto> boilerTypeAdditionalDataSetDto) {
        try {
            boilerTypeAdditionalValueService.updateAdditionalValue(boilerTypeAdditionalDataSetDto);
        }
        catch (DataAccessException e) {
            log.error("Ошибка при обновлении набора данных котла", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
