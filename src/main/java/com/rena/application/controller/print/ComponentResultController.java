package com.rena.application.controller.print;

import com.rena.application.entity.dto.print.ComponentsResults;
import com.rena.application.service.print.ComponentResultService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;

@BrowserCallable
@Validated
@RequiredArgsConstructor
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
public class ComponentResultController {
    private final ComponentResultService componentResultService;

    @Nonnull
    public ComponentsResults getComponentsByOperationId(@Nonnull Long operationId) {
        return componentResultService.getComponentsByOperationId(operationId);
    }
}
