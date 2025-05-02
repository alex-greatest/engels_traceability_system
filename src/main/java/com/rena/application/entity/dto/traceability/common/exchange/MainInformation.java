package com.rena.application.entity.dto.traceability.common.exchange;

import com.rena.application.entity.dto.settings.user.UserResponse;
import com.rena.application.entity.dto.traceability.common.boiler.BoilerMadeInformation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class MainInformation extends RpcBase {
    private final BoilerMadeInformation boilerMadeInformation;
    private final UserResponse operator;
    private final UserResponse admin;
}
