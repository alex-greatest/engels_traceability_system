package com.rena.application.controller.print;

import com.rena.application.entity.dto.print.OperationResult;
import com.rena.application.service.print.OperationResultService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@BrowserCallable
@Validated
@RequiredArgsConstructor
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
public class OperationControllerResult {
    private final OperationResultService operationResultService;

    @Nonnull
    public List<@Nonnull OperationResult> getOperationsBySerial(@Nonnull String serialNumber) {
        return operationResultService.getOperationsBySerial(serialNumber);
    }
}
