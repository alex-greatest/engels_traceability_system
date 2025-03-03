package com.rena.application.controller.component.binding;

import com.rena.application.entity.dto.component.ComponentNameBindingDto;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.component.binding.ComponentNameBindingService;
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
public class ComponentNameBindingController {
    private final ComponentNameBindingService componentNameBindingService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull ComponentNameBindingDto> getAllComponentsNameBinding() {
        return componentNameBindingService.getAllComponentsNameBinding();
    }

    @Nonnull
    public ComponentNameBindingDto getComponentNameBinding(@Nonnull Long id)
    {
        return componentNameBindingService.getComponentNameBinding(id);
    }

    public void addComponentNameBinding(@Nonnull @Valid ComponentNameBindingDto componentNameBindingDto) {
        try {
            componentNameBindingService.addComponentBindingName(componentNameBindingDto);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении имени набора привязки компонентов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateComponentNameBinding(@Nonnull @NotNull Long id,
                                           @Nonnull @Valid ComponentNameBindingDto componentNameBindingDto) {
        try {
            componentNameBindingService.updateComponentBindingName(id, componentNameBindingDto);
        }
        catch (DataAccessException e) {
            log.error("Ошибка при добавлении имени набора привязки компонентов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteComponentNameBinding(@Nonnull @NotNull Long id) {
        try {
            componentNameBindingService.deleteComponentBindingName(id);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении имени набора привязки компонентов", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
