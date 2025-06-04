package com.rena.application.entity.dto.traceability.label;

import java.util.List;
import com.rena.application.entity.dto.traceability.common.exchange.RpcBase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ListLabelValue extends RpcBase {
    private final List<LabelValueDto> labelValues;
}