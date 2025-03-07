package com.rena.application.controller.component;

import com.rena.application.entity.dto.component.ComponentBindingRequest;
import com.rena.application.entity.dto.component.ComponentBindingResponse;
import com.rena.application.exceptions.DbException;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.component.binding.ComponentBindingService;
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
public class ComponentBindingController {
    private final ComponentBindingService componentBindingService;
    private final HandlerErrorConstraintDB handlerErrorConstraintDB;

    @Nonnull
    public List<@Nonnull ComponentBindingResponse> getAllBindingComponents(@Nonnull @NotNull String nameStation,
                                                                           @Nonnull @NotNull Long idNameSet) {
        return componentBindingService.getAllComponentsBinding(nameStation, idNameSet);
    }

    public void addComponentBinding(@Valid @Nonnull ComponentBindingRequest componentBindingRequest) {
        try {
            componentBindingService.addComponentBinding(componentBindingRequest);
        } catch (DataAccessException e) {
            log.error("Ошибка при добавлении привязки", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void updateComponentBinding(@Valid @Nonnull List<@Nonnull ComponentBindingRequest> componentBindingRequest) {
        try {
            componentBindingService.updateComponentBinding(componentBindingRequest);
        } catch (DataAccessException e) {
            log.error("Ошибка при обновлении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }

    public void deleteComponentBinding(@Nonnull @NotNull Long componentSetId) {
        try {
            componentBindingService.deleteComponentBinding(componentSetId);
        } catch (DataAccessException e) {
            log.error("Ошибка при удалении компонента", e);
            String message = handlerErrorConstraintDB.findMessageError(e.getMessage());
            throw new DbException(message);
        }
    }
}
