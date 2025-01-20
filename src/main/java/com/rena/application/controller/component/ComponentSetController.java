package com.rena.application.controller.component;

import com.rena.application.entity.dto.component.ComponentSetDtos;
import com.rena.application.service.component.ComponentSetService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

@RequiredArgsConstructor
@BrowserCallable
@Validated
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
@Slf4j
public class ComponentSetController {
    private final ComponentSetService componentSetService;

    @Nonnull
    public ComponentSetDtos getAllComponentSet(@Nonnull Long id) {
        return componentSetService.getAllComponents(id);
    }
}
