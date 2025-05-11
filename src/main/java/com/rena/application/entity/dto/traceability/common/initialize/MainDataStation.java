package com.rena.application.entity.dto.traceability.common.initialize;

import com.rena.application.entity.dto.settings.user.UserResponse;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MainDataStation extends RpcBase {
    private final UserResponse operator;
    private final UserResponse admin;
    private final Integer shiftNumber;
}
