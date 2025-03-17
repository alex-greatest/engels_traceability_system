package com.rena.application.entity.dto.result.station.wp.components;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ComponentsResultRequest(@NotNull List<ComponentsResult> componentsResult,
                                      @NotBlank String serialNumber,
                                      @NotBlank String stationName,
                                      @NotBlank String status) {
}
