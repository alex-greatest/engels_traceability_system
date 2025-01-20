package com.rena.application.controller.component;

import com.rena.application.entity.dto.component.ComponentNameSetDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.component.ComponentNameSetService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class ComponentNameSetController {
    private final ComponentNameSetService componentNameSetService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull ComponentNameSetDto> getAllComponents() {
        return componentNameSetService.getAllComponents();
    }

    @Nonnull
    public ComponentNameSetDto getComponent(@Nonnull Long id)
    {
        return componentNameSetService.getComponent(id);
    }

    public void addComponent(@Valid ComponentNameSetDto componentNameSetDto) {
        try {
            componentNameSetService.addComponent(componentNameSetDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении имени набора компонентов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateComponent(@Nonnull Long id, @NotBlank String oldNameComponent, @Valid ComponentNameSetDto componentNameSetDto) {
        try {
            componentNameSetService.updateComponent(id, oldNameComponent, componentNameSetDto);
        }
        catch (DataAccessException e) {
            log.error("Ошибка при обновлении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteComponent(@Nonnull Long id) {
        try {
            componentNameSetService.deleteComponent(id);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
