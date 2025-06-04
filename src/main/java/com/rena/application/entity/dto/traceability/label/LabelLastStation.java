package com.rena.application.entity.dto.traceability.label;

import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class LabelLastStation extends RpcBase {
    private final String labelName;
    private final List<LabelValueDto> labelValues;
}
