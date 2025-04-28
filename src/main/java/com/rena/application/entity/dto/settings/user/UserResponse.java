package com.rena.application.entity.dto.settings.user;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.settings.user.User;
import com.vaadin.hilla.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link User}
 */
@Getter
@Setter
public class UserResponse extends RpcBase {
    @NotBlank @Size(max = 50) private final String username;

    @NotNull private final Integer code;

    @Nonnull @NotNull final private RoleDTO role;

    public UserResponse(String username, Integer code, RoleDTO role) {
        this.username = username;
        this.code = code;
        this.role = role;
    }
}