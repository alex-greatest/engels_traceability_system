package com.rena.application.entity.dto.traceability.common.initialize;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ShiftNumber extends RpcBase {
    private final Integer shiftNumber;
}
