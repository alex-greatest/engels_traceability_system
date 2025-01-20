package com.rena.application.controller.user;

import com.rena.application.entity.dto.user.RoleDTO;
import com.rena.application.service.user.RoleService;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.Nonnull;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import java.util.List;

@RequiredArgsConstructor
@BrowserCallable
@Validated
@RolesAllowed({"ROLE_Администратор", "ROLE_Инженер МОЕ", "ROLE_Инженер TEF"})
public class RoleController {
    private final RoleService roleService;

    @Nonnull
    public List<@Nonnull RoleDTO> getAllRoles() {
        return roleService.getAllRoles();
    }
}
