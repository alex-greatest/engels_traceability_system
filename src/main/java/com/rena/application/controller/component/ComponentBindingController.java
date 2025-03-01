package com.rena.application.controller.component;

import com.rena.application.entity.dto.component.ComponentBindingDto;
import com.rena.application.service.HandlerErrorConstraintDB;
import com.rena.application.service.component.ComponentBindingService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<@Nonnull ComponentBindingDto> getAllBindingComponents(@Nonnull @NotNull String nameStation) {
        return componentBindingService.getAllComponentsBinding(nameStation);
    }
}
