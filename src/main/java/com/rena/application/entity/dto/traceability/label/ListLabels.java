package com.rena.application.entity.dto.traceability.label;

import java.util.List;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ListLabels extends RpcBase {
    private final List<String> labels;
}
