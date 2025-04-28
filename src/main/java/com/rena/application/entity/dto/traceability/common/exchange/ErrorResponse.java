package com.rena.application.entity.dto.traceability.common.exchange;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse extends RpcBase {
    private final String message;
}
