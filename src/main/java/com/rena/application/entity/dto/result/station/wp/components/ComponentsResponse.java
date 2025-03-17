package com.rena.application.entity.dto.result.station.wp.components;

import com.rena.application.entity.dto.component.ComponentBindingResponse;
import com.rena.application.entity.dto.component.ComponentSetDto;
import com.rena.application.entity.dto.result.common.BoilerTypeCycleStation;
import com.rena.application.entity.model.result.common.Boiler;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO for {@link Boiler}
 */
public record ComponentsResponse(@NotNull BoilerTypeCycleStation boilerTypeCycle,
                                 @NotNull List<ComponentSetDto> componentSetDtoList,
                                 @NotNull List<ComponentBindingResponse> componentBindingResponses) {
}