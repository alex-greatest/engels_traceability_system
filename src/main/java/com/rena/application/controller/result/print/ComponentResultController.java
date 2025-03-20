package com.rena.application.controller.result.print;

import com.rena.application.entity.dto.result.print.ComponentsResults;
import com.rena.application.service.result.print.ComponentResultService;
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
