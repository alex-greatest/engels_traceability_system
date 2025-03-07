package com.rena.application.controller.component;

import com.rena.application.entity.dto.component.ComponentTypeDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.component.ComponentTypeService;
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
public class ComponentTypeController {
    private final ComponentTypeService componentTypeService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull ComponentTypeDto> getAllComponents() {
        return componentTypeService.getAllComponents();
    }

    @Nonnull
    public List<@Nonnull ComponentTypeDto> getAllComponentsTypeSet(Long nameSetId) {
        return componentTypeService.getAllComponentsTypeSet(nameSetId);
    }

    @Nonnull
    public ComponentTypeDto getComponent(@Nonnull @NotNull Long id)
    {
        return componentTypeService.getComponent(id);
    }

    public void addComponent(@Valid ComponentTypeDto componentTypeDto) {
        try {
            componentTypeService.addComponent(componentTypeDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении типа компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateComponent(@Nonnull @NotNull Long id, @Valid ComponentTypeDto componentTypeDto) {
        try {
            componentTypeService.updateComponent(id, componentTypeDto);
        }
        catch (DataAccessException e) {
            log.error("Ошибка при обновлении типа компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteComponent(@Nonnull @NotNull Long id) {
        try {
            componentTypeService.deleteComponent(id);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении типа компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
