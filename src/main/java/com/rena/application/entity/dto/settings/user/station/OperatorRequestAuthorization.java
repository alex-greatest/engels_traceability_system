package com.rena.application.entity.dto.settings.user.station;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import com.rena.application.entity.model.settings.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link User}
 */
@Getter
@Setter
public class OperatorRequestAuthorization extends RpcBase {
    @NotBlank private final String login;
    @NotBlank private final String password;
    @NotBlank private final String station;

    public OperatorRequestAuthorization(@NotBlank String login,
                                        @NotBlank String password,
                                        @NotBlank String station) {
        super();
        this.login = login;
        this.password = password;
        this.station = station;
    }
}