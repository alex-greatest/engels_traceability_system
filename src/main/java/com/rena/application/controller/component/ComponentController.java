package com.rena.application.controller.component;

import com.rena.application.entity.dto.component.ComponentDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.component.ComponentService;
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
public class ComponentController {
    private final ComponentService componentService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull ComponentDto> getAllComponents() {
        return componentService.getAllComponents();
    }

    @Nonnull
    public ComponentDto getComponent(@Nonnull Long id)
    {
        return componentService.getComponent(id);
    }

    public void addComponent(@Valid ComponentDto componentDto) {
        try {
            componentService.addComponent(componentDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateComponent(@Nonnull Long id, @NotBlank String oldNameComponent, @Valid ComponentDto componentDto) {
        try {
            componentService.updateComponent(id, oldNameComponent, componentDto);
        }
        catch (DataAccessException e) {
            log.error("Ошибка при обновлении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteComponent(@Nonnull Long id) {
        try {
            componentService.deleteComponent(id);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
