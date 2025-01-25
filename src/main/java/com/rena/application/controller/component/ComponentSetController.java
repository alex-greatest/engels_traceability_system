package com.rena.application.controller.component;

import com.rena.application.entity.dto.component.ComponentSetDto;
import com.rena.application.entity.dto.component.ComponentSetList;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.component.ComponentSetService;
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
public class ComponentSetController {
    private final ComponentSetService componentSetService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public ComponentSetList getAllComponentSet(@Nonnull @NotNull Long id) {
        return componentSetService.getAllComponentsSet(id);
    }

    public void addComponentSet(@Nonnull @NotNull Long componentNameSetId,
                                @Valid ComponentSetDto componentSetDto) {
        try {
            componentSetService.addComponentSet(componentNameSetId, componentSetDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении компонента в набор", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateComponentSet(@Nonnull @NotNull List<@Valid ComponentSetDto> componentsSetRequest) {
        try {
            componentSetService.updateComponentSet(componentsSetRequest);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении компонента в набор", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteComponent(@Nonnull @NotNull Long componentSetId) {
        try {
            componentSetService.deleteComponentSet(componentSetId);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении имени набора компонентов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void copyAllComponentsType(@Nonnull @NotNull Long componentSetId) {
        componentSetService.copyAllComponentsType(componentSetId);
    }
}
